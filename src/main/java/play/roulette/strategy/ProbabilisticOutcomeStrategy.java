package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProbabilisticOutcomeStrategy {

    protected ProbabilisticOutcomeStrategy(Statistics statistics) {
        this.statistics = statistics;
    }
    private Statistics statistics = null;
    public abstract String getStrategyName();
    public abstract Set<RouletteNumber> getProbables();

    protected Statistics getStatistics() {
        return statistics;
    }

    @Override
    public String toString() {
        return "According to " + getStrategyName() + " probable numbers are " + getProbables().stream().map(RouletteNumber::getBoxedNumber).collect(
                Collectors.joining(", "));
    }

    public boolean win(RouletteNumber outcome) {
        return getProbables().contains(outcome);
    }
}
