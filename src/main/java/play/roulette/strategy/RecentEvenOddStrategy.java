package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import static play.roulette.Constants.evenOddRouletteNumbers;

public class RecentEvenOddStrategy extends ProbabilisticOutcomeStrategy {
    private final int WINDOW_SIZE = 10;
    private final String strategyName = "Recent Even Odd Strategy";
    private Set<RouletteNumber> probables = null;

    public RecentEvenOddStrategy(Statistics statistics) {
        super(statistics);
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
        int recentEvens = recentEvens(recentOutcomes);

        // If there is a conflicting pattern, guess the opposite of the last outcome
        if (recentEvens == 0) {
            recentEvens = lastWasEven(previousOutcomes.peek());
        }

        // Otherwise, check for the presence of at least 3 even or odd numbers
        if (recentEvens == 1) {
            predictedNumbers = evenOddRouletteNumbers(true);
        } else if (recentEvens == -1) {
            predictedNumbers = evenOddRouletteNumbers(false);
        } else {
            return Collections.emptySet();
        }
        this.probables = predictedNumbers;
        return this.probables;
    }

    private int recentEvens(List<RouletteNumber> outcomes) {
        int recentEvenCount = 0;
        int recentOddCount = 0;

        for (RouletteNumber outcome : outcomes) {
            if (outcome.isEven()) {
                recentEvenCount++;
            } else {
                recentOddCount++;
            }
            if (recentEvenCount >= 3) {
                return 1;
            }
            if (recentOddCount >= 3) {
                return -1;
            }
        }

        return 0;
    }

    private int lastWasEven(RouletteNumber lastOutcome) {
        return lastOutcome.isZero() ? 0 : lastOutcome.isEven() ? 1 : -1;
    }

    @Override
    public String getStrategyName() {
        return strategyName;
    }
}

