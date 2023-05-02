package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import static play.roulette.Constants.blackRedRouletteNumbers;

public class RecentBlackRedStrategy extends ProbabilisticOutcomeStrategy {
    private final int WINDOW_SIZE = 10;
    private final String strategyName = "Recent Black Red Strategy";
    private Set<RouletteNumber> probables = null;

    public RecentBlackRedStrategy(Statistics statistics) {
        super(statistics);
    }

    @Override
    public String getStrategyName() {
        return strategyName;
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        ConcurrentLinkedQueue<RouletteNumber> previousOutcomes = this.getStatistics().getPreviousOutcomes();
        Set<RouletteNumber> predictedNumbers = null;

        if (previousOutcomes == null || previousOutcomes.size() == 0) return null;

        // Check if there are enough previous outcomes to make a prediction
        if (previousOutcomes.size() < WINDOW_SIZE) {
            return Collections.emptySet();
        }

        // Get the most recent outcomes
        List<RouletteNumber> recentOutcomes = new ArrayList<>(previousOutcomes).subList(previousOutcomes.size() - WINDOW_SIZE, previousOutcomes.size());

        // Check for the presence of conflicting patterns in the recent outcomes
        int recentBlacks = recentBlacks(recentOutcomes);

        // If there is a conflicting pattern, guess the opposite of the last outcome
        if (recentBlacks == 0) {
            recentBlacks = lastWasBlack(previousOutcomes.peek());
        }

        // Otherwise, check for the presence of at least 3 black or red numbers
        if (recentBlacks == 1) {
            predictedNumbers = blackRedRouletteNumbers(true);
        } else if (recentBlacks == -1) {
            predictedNumbers = blackRedRouletteNumbers(false);
        } else {
            return Collections.emptySet();
        }
        this.probables = predictedNumbers;
        return this.probables;
    }

    private int recentBlacks(List<RouletteNumber> outcomes) {
        int recentBlackCount = 0;
        int recentRedCount = 0;

        for (RouletteNumber outcome : outcomes) {
            if (!(outcome.isRed() || outcome.isGreen())) {
                recentBlackCount++;
            } else if (outcome.isRed()) {
                recentRedCount++;
            }
            if (recentBlackCount >= 3) {
                return 1;
            }
            if (recentRedCount >= 3) {
                return -1;
            }
        }

        return 0;
    }

    private int lastWasBlack(RouletteNumber lastOutcome) {
        return lastOutcome.isZero() ? 0 : lastOutcome.isRed() ? -1 : 1;
    }
}

