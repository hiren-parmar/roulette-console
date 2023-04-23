package play.roulette.strategy;

import play.roulette.Constants;
import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static play.roulette.Constants.allRouletteNumbers;

public class LawOfThirdsStrategy extends ProbabilisticOutcomeStrategy {

    private final int NUM_LAST_OUTCOMES_TO_CONSIDER = 12;

    public LawOfThirdsStrategy(Statistics statistics) {
        super(statistics);
    }

    @Override
    public String getStrategyName() {
        return "The Law of Thirds";
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        List<RouletteNumber> previousOutcomes = getStatistics().getPreviousOutcomes();
        Map<RouletteNumber, Integer> previousOutcomesWithCount = getStatistics().getPreviousOutcomesWithCount();
        int numOutcomes = previousOutcomes.size();

        Set<RouletteNumber> hotNumbers = new HashSet<>();
        Set<RouletteNumber> coldNumbers = new HashSet<>();

        // consider only the last NUM_LAST_OUTCOMES_TO_CONSIDER outcomes
        int start = Math.max(numOutcomes - NUM_LAST_OUTCOMES_TO_CONSIDER, 0);
        List<RouletteNumber> lastOutcomes = previousOutcomes.subList(start, numOutcomes);

        // get the frequency count for each number in the last outcomes
        Map<RouletteNumber, Integer> lastOutcomesWithCount = new HashMap<>();
        for (RouletteNumber number : lastOutcomes) {
            lastOutcomesWithCount.put(number, lastOutcomesWithCount.getOrDefault(number, 0) + 1);
        }

        // calculate the expected frequency of each number based on the Law of Thirds
        double expectedFrequency = NUM_LAST_OUTCOMES_TO_CONSIDER / 3.0;

        // identify hot and cold numbers based on the difference between actual and expected frequency
        for (RouletteNumber number : allRouletteNumbers()) {
            int actualFrequency = previousOutcomesWithCount.getOrDefault(number, 0);
            int lastFrequency = lastOutcomesWithCount.getOrDefault(number, 0);
            if (actualFrequency > expectedFrequency && lastFrequency > 0) {
                hotNumbers.add(number);
            } else if (actualFrequency < expectedFrequency && lastFrequency == 0) {
                coldNumbers.add(number);
            }
        }

        // if no hot or cold numbers found, return all the numbers
        if (hotNumbers.isEmpty() && coldNumbers.isEmpty()) {
            return Constants.allRouletteNumbers();
        }

        // return hot or cold numbers, based on which list is shorter
        if (hotNumbers.size() <= coldNumbers.size()) {
            return hotNumbers;
        } else {
            return coldNumbers;
        }
    }
}

