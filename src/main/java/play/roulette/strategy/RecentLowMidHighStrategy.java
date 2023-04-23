package play.roulette.strategy;

import play.roulette.LowMidHighRange;
import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static play.roulette.Constants.lowMidHighRouletteNumbers;

public class RecentLowMidHighStrategy extends ProbabilisticOutcomeStrategy {
    private final int WINDOW_SIZE = 10;
    private final String strategyName = "Recent Low Mid High Strategy";
    private Set<RouletteNumber> probables = null;

    public RecentLowMidHighStrategy(Statistics statistics) {
        super(statistics);
    }

    @Override
    public String getStrategyName() {
        return strategyName;
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        List<RouletteNumber> previousOutcomes = this.getStatistics().getPreviousOutcomes();
        Set<RouletteNumber> predictedNumbers = null;

        if (previousOutcomes == null || previousOutcomes.size() == 0) return null;

        // Check if there are enough previous outcomes to make a prediction
        if (previousOutcomes.size() < WINDOW_SIZE) {
            return Collections.emptySet();
        }

        // Get the most recent outcomes
        List<RouletteNumber> recentOutcomes = previousOutcomes.subList(previousOutcomes.size() - WINDOW_SIZE, previousOutcomes.size());

        // Check for the presence of conflicting patterns in the recent outcomes
        int recentLowCount = recentCountInRange(recentOutcomes, 1, 12);
        int recentMidCount = recentCountInRange(recentOutcomes, 13, 24);
        int recentHighCount = recentCountInRange(recentOutcomes, 25, 36);

        // If there is a conflicting pattern, guess the opposite of the last outcome
        if (recentLowCount > 0 && recentMidCount > 0 && recentHighCount > 0) {
            int lastOutcome = Integer.parseInt(previousOutcomes.get(previousOutcomes.size() - 1).getNumber());
            if (lastOutcome <= 12) {
                predictedNumbers = lowMidHighRouletteNumbers(LowMidHighRange.LOW);
            } else if (lastOutcome > 12 && lastOutcome <= 24) {
                predictedNumbers = lowMidHighRouletteNumbers(LowMidHighRange.MID);
            } else {
                predictedNumbers = lowMidHighRouletteNumbers(LowMidHighRange.HIGH);
            }
        }

        // Otherwise, check for the presence of at least 3 numbers in one of the ranges
        if (predictedNumbers == null) {
            if (recentLowCount >= 3) {
                predictedNumbers = lowMidHighRouletteNumbers(LowMidHighRange.LOW);
            } else if (recentMidCount >= 3) {
                predictedNumbers = lowMidHighRouletteNumbers(LowMidHighRange.MID);
            } else if (recentHighCount >= 3) {
                predictedNumbers = lowMidHighRouletteNumbers(LowMidHighRange.HIGH);
            }
        }

        this.probables = predictedNumbers;
        return this.probables;
    }

    private int recentCountInRange(List<RouletteNumber> outcomes, int start, int end) {
        int recentCount = 0;

        for (RouletteNumber outcome : outcomes) {
            if (Integer.parseInt(outcome.getNumber()) >= start && Integer.parseInt(outcome.getNumber()) <= end) {
                recentCount++;
            }
        }

        return recentCount;
    }

    private List<RouletteNumber> rangeRouletteNumbers(int start, int end) {
        List<RouletteNumber> rouletteNumbers = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            rouletteNumbers.add(new RouletteNumber(String.valueOf(i)));
        }
        return rouletteNumbers;
    }

    @Override
    public String toString() {
        return this.strategyName;
    }
}

