package play.roulette.concurrent;

import play.roulette.Game;
import play.roulette.RouletteNumber;
import play.roulette.strategy.ProbabilisticOutcomeStrategy;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SimulationTask implements Callable<SimulationResult> {
    private final int startRound;
    private final int endRound;
    private final Game game;
    private final Set<ProbabilisticOutcomeStrategy> strategies;

    public SimulationTask(Game game, Set<ProbabilisticOutcomeStrategy> strategies, int startRound, int endRound) {
        this.startRound = startRound;
        this.endRound = endRound;
        this.game = game;
        this.strategies = strategies;
    }

    public SimulationResult call() {
        SimulationResult result = new SimulationResult();
        for (int i = startRound; i < endRound; i++) {
            // Simulate a single round of the game
            RouletteNumber outcome = game.play();

            // Check each strategy to see if its predicted outcomes match the actual outcome
            for (ProbabilisticOutcomeStrategy strategy : strategies) {
                if (strategy.win(outcome)) {
                    result.incrementScore(strategy);
                }
            }
        }
        return result;
    }
}