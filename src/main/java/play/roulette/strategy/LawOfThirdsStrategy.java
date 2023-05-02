package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static play.roulette.Constants.allRouletteNumbersAsList;

public class LawOfThirdsStrategy extends ProbabilisticOutcomeStrategy {
    private final int numSpins;
    private final int numOutcomes;

    public LawOfThirdsStrategy(Statistics statistics) {
        super(statistics);
        this.numSpins = statistics.getPreviousOutcomes().size();
        this.numOutcomes = statistics.getPreviousOutcomes().size();
    }

    @Override
    public String getStrategyName() {
        return "Law of Thirds";
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        Set<RouletteNumber> probables = new HashSet<>();

        if (numOutcomes == 0) {
            probables.addAll(allRouletteNumbersAsList());
        } else {
            int start = Math.max(numOutcomes - (numSpins / 3), 0);
            List<RouletteNumber> previousOutcomes = new ArrayList<>(getStatistics().getPreviousOutcomes());
            for (int i = start; i < numOutcomes; i++) {
                RouletteNumber outcome = previousOutcomes.get(i);
                probables.add(outcome);
            }
        }

        return probables;
    }
}


