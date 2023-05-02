package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static play.roulette.Constants.EVEN_KEY;
import static play.roulette.Constants.ODD_KEY;
import static play.roulette.Constants.evenOddRouletteNumbers;

public class EvenOddStrategy extends ProbabilisticOutcomeStrategy {
    private final String strategyName = "Even Odd Strategy";
    private Set<RouletteNumber> probables = null;

    public EvenOddStrategy(Statistics statistics) {
        super(statistics);
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        Set<RouletteNumber> predictedNumbers = new ConcurrentSkipListSet<>();
        Map<String, Integer> evenOddStats = this.getStatistics().getEvenOddStats();
        if (evenOddStats == null || evenOddStats.size() == 0) return null;
        int evenCount = evenOddStats.containsKey(EVEN_KEY) ? evenOddStats.get(EVEN_KEY) : 0;
        int oddCount = evenOddStats.containsKey(ODD_KEY) ? evenOddStats.get(ODD_KEY) : 0;
        if (evenCount > oddCount) {
            evenOddRouletteNumbers(true);
        } else {
            evenOddRouletteNumbers(false);
        }
        this.probables = predictedNumbers;
        return this.probables;
    }

    @Override
    public String getStrategyName() {
        return strategyName;
    }
}
