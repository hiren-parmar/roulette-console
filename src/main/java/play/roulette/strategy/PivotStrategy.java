package play.roulette.strategy;

import play.roulette.Constants;
import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import static play.roulette.Constants.PIVOT_MAX_LOSSES;
import static play.roulette.Constants.allRouletteNumbers;

public class PivotStrategy extends ProbabilisticOutcomeStrategy {

    private int pivot;
    //In the context of the Pivot Strategy, the pivot variable is the number that is chosen as the initial bet. The pivot number is usually identified by looking at the previous spins and identifying the number that has appeared twice. For example, if the previous spins were 5, 12, 10, 19, 10, 28, 5, then the number 10 has appeared twice and would be chosen as the pivot number. The idea behind the strategy is to bet on the pivot number and continue betting on it until a win occurs. Once the pivot number hits, the strategy restarts with a new pivot number chosen from the previous spins.
    private int consecutiveLosses;
    //consecutiveLosses is a variable that tracks the number of consecutive losses. In the bet() method of PivotStrategy, it is incremented whenever the previous bet loses, and reset to 0 whenever the previous bet wins. This is used to determine when to stop using the pivot bet and switch to a new one. Specifically, when consecutiveLosses reaches pivotMaxLosses, the strategy switches to the next pivot bet in Constants.pivotBets.

    public PivotStrategy(Statistics statistics) {
        super(statistics);
        this.pivot = 10;
        this.consecutiveLosses = 0;
    }

    @Override
    public String getStrategyName() {
        return "Pivot Strategy";
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        ConcurrentLinkedQueue<RouletteNumber> previousOutcomes = getStatistics().getPreviousOutcomes();
        Set<RouletteNumber> probables = new ConcurrentSkipListSet<>();
        if (previousOutcomes.size() < 1) {
            return allRouletteNumbers();
        }
        RouletteNumber lastResult = previousOutcomes.peek();
        if (lastResult.getNumeric() == this.pivot) {
            int i = previousOutcomes.size() - 2;

            RouletteNumber[] previousOutcomesAsArray = (RouletteNumber[]) previousOutcomes.toArray();
            while (i >= 0 && previousOutcomesAsArray[i].getNumeric() != this.pivot) {
                i--;
            }
            if (i < 0) {
                // No pivot found, return all numbers
                return allRouletteNumbers();
            }
            int betIndex = (i + 1) % Constants.allRouletteNumbers().size();
            RouletteNumber bet = Constants.allRouletteNumbersAsList().get(betIndex);
            probables.add(bet);
            return probables;
        } else {
            this.consecutiveLosses++;
            if (this.consecutiveLosses >= PIVOT_MAX_LOSSES) {
                this.consecutiveLosses = 0;
                int betIndex = previousOutcomes.size() % Constants.allRouletteNumbers().size();
                RouletteNumber bet = Constants.allRouletteNumbersAsList().get(betIndex);
                probables.add(bet);
                return probables;
            } else {
                return allRouletteNumbers();
            }
        }
    }
}

