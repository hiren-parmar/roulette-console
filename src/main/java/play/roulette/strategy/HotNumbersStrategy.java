package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static play.roulette.Constants.MAX_ROULETTE_NUMBER;

public class HotNumbersStrategy extends ProbabilisticOutcomeStrategy {
    public HotNumbersStrategy(Statistics statistics) {
        super(statistics);
    }

    @Override
    public String getStrategyName() {
        return "Hot Numbers Strategy";
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        List<RouletteNumber> previousOutcomes = this.getStatistics().getPreviousOutcomes();
        Set<RouletteNumber> predictedNumbers = new HashSet<>();

        if (previousOutcomes == null || previousOutcomes.isEmpty()) return null;

        // Calculate the average number of times each number has occurred in the past
        double averageCount = (double) previousOutcomes.size() / MAX_ROULETTE_NUMBER;

        // Get the recent outcomes with counts
        List<RouletteNumber> recentOutcomes = new ArrayList<>();
        for (Map.Entry<RouletteNumber, Integer> entry : this.getStatistics().getPreviousOutcomesWithCount().entrySet()) {
            if (entry.getValue() > averageCount) {
                recentOutcomes.add(entry.getKey());
            }
        }

        // If there are not enough recent outcomes, return empty list
        if (recentOutcomes.size() < 3) {
            return Collections.emptySet();
        }

        // Check for hot numbers
        for (RouletteNumber hotNumber : this.getStatistics().getHotNumbers()) {
            if (recentOutcomes.contains(hotNumber)) {
                predictedNumbers.add(hotNumber);
            }
        }

        return predictedNumbers;
    }
}
