package play.roulette.concurrent;

import play.roulette.strategy.ProbabilisticOutcomeStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationResult {

    private int roundsPlayed;
    private final Map<ProbabilisticOutcomeStrategy, Integer> winsByStrategy;

    public SimulationResult() {
        this.roundsPlayed = 0;
        this.winsByStrategy = new ConcurrentHashMap<>();
    }

    public void displayResults() {
        // Code to display results in formatted table
    }

    public void incrementScore(ProbabilisticOutcomeStrategy strategy) {
        int count = this.winsByStrategy.getOrDefault(strategy, 0);
        this.winsByStrategy.put(strategy, 1 + count);
        roundsPlayed++;
    }

    // Getters and setters for instance variables
    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public Map<ProbabilisticOutcomeStrategy, Integer> getWinsByStrategy() {
        return winsByStrategy;
    }
}

