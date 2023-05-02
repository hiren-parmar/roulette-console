package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class OutcomeProbabilityStrategy extends ProbabilisticOutcomeStrategy {
    private final String strategyName = "Outcome Probability Strategy";
    private Set<RouletteNumber> probables = null;

    public OutcomeProbabilityStrategy(Statistics statistics) {
        super(statistics);
    }

    @Override
    public String getStrategyName() {
        return strategyName;
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        ConcurrentLinkedQueue<RouletteNumber> previousOutcomes = this.getStatistics().getPreviousOutcomes();
        Map<RouletteNumber, Integer> frequencyMap = new ConcurrentHashMap<>();

        // Count the frequency of each number's occurrence in the previous outcomes
        for (RouletteNumber outcome : previousOutcomes) {
            frequencyMap.put(outcome, frequencyMap.getOrDefault(outcome, 0) + 1);
        }

        // Calculate the probability of each number appearing in the next outcome
        Map<RouletteNumber, Double> probabilityMap = new ConcurrentHashMap<>();
        int totalOutcomes = previousOutcomes.size();
        for (RouletteNumber number : previousOutcomes) {
            int frequency = frequencyMap.getOrDefault(number, 0);
            double probability = (double) frequency / totalOutcomes;
            probabilityMap.put(number, probability);
        }

//        System.out.println(probabilityMap.keySet().stream()
//                .map(n -> "[" + n.getBoxedNumber() + ":"
//                        + numberAsSuperscript(String.format("%02.0f", probabilityMap.get(n)))
//                        + numberAsSubscript((probabilityMap.get(n) * totalOutcomes) + "]"))
//                .collect(Collectors.joining(", ")));

        // Sort the numbers in descending order of probability and return the top 3
        this.probables = probabilityMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return this.probables;
    }
}

