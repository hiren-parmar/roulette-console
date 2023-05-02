package play.roulette.concurrent;

import play.roulette.Game;
import play.roulette.strategy.ProbabilisticOutcomeStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimulationManager {
    private final ExecutorService executorService;
    private final BlockingQueue<SimulationResult> resultQueue;
    private final List<Callable<SimulationResult>> simulationTasks;

    public SimulationManager(Game game, Set<ProbabilisticOutcomeStrategy> strategies, int numThreads, int numRounds) {
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.resultQueue = new LinkedBlockingQueue<>();
        this.simulationTasks = new ArrayList<>();

        int roundsInEachThread = numRounds / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i + 1 + (i * roundsInEachThread);
            int end = roundsInEachThread * (i + 1);
            simulationTasks.add(new SimulationTask(game, strategies, start, end));
        }
    }

    public List<SimulationResult> runSimulation() throws InterruptedException {
        List<Future<SimulationResult>> futures = executorService.invokeAll(simulationTasks);
        for (Future<SimulationResult> future : futures) {
            try {
                resultQueue.put(future.get());
            } catch (ExecutionException e) {
                // handle any exceptions that occurred during execution of the tasks
                e.printStackTrace();
            }
        }

        // wait for all threads to finish
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        List<SimulationResult> results = new ArrayList<>();
        resultQueue.drainTo(results);
        return results;
    }

    public void runSimulationAndShowResult(Map<ProbabilisticOutcomeStrategy, Integer> strategyPoints)
            throws InterruptedException {
        // TODO: aggregate the results
        List<SimulationResult> results = runSimulation();
        for (SimulationResult result : results) {
            Map<ProbabilisticOutcomeStrategy, Integer> resultsWinByStrategy = result.getWinsByStrategy();
            for (ProbabilisticOutcomeStrategy strategy : resultsWinByStrategy.keySet()) {
                int count = strategyPoints.getOrDefault(strategy, 0);
                int resultCount = resultsWinByStrategy.getOrDefault(strategy, 0);
                strategyPoints.put(strategy, resultCount + count);
            }
        }

        for (Map.Entry<ProbabilisticOutcomeStrategy, Integer> strategyPointsEntry : strategyPoints.entrySet()) {
            System.out.println(strategyPointsEntry.getKey() + ":" + strategyPointsEntry.getValue());
        }
    }
}


