package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static play.roulette.Constants.topBottomRouletteNumbers;

public class RecentTopBottomStrategy extends ProbabilisticOutcomeStrategy {
    private final String strategyName = "Top Bottom Strategy";
    private Set<RouletteNumber> probables = null;

    public RecentTopBottomStrategy(Statistics statistics) {
        super(statistics);
    }

    @Override
    public String getStrategyName() {
        return this.strategyName;
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        List<RouletteNumber> previousOutcomes = this.getStatistics().getPreviousOutcomes();
        Set<RouletteNumber> predictedNumbers = null;

        if (previousOutcomes == null || previousOutcomes.size() == 0) return null;

        // Get the most recent outcome
        RouletteNumber recentOutcome = previousOutcomes.get(previousOutcomes.size() - 1);

        // Check if the recent outcome falls in the top or bottom half of numbers
        if (recentOutcome.isTop()) {
            predictedNumbers = topBottomRouletteNumbers(true);
        } else if (recentOutcome.isBottom()) {
            predictedNumbers = topBottomRouletteNumbers(false);
        } else {
            return Collections.emptySet();
        }
        this.probables = predictedNumbers;
        return this.probables;
    }


    @Override
    public String toString() {
        return strategyName;
    }
}
