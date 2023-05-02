package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class LawOfLargeNumbersStrategy extends ProbabilisticOutcomeStrategy {

    private int sampleSize;

    public LawOfLargeNumbersStrategy(Statistics statistics) {
        super(statistics);
        this.sampleSize = 100;
    }

    @Override
    public String getStrategyName() {
        return "Law of Large Numbers";
    }

    @Override
    public Set<RouletteNumber> getProbables() {
        Statistics stats = getStatistics();
        Set<RouletteNumber> probables = new ConcurrentSkipListSet<>();
        Map<RouletteNumber, Integer> previousOutcomesWithCount = stats.getPreviousOutcomesWithCount();
        int totalOutcomes = stats.getNumberOfGames();
//        int sampleCount = Math.min(sampleSize, totalOutcomes);
//        List<RouletteNumber> previousOutcomes = stats.getPreviousOutcomes().subList(Math.max(0, totalOutcomes - sampleCount), totalOutcomes);
        for (RouletteNumber rn : previousOutcomesWithCount.keySet()) {
            int outcomeCount = previousOutcomesWithCount.get(rn);
            double probability = outcomeCount / (double) totalOutcomes;
            rn.setProbability(probability);
            probables.add(rn);
            rn.getProbability();
        }
//        Collections.sort(probables, (rn1, rn2) -> Double.compare(new Double(rn2.getProbability()), rn1.getProbability()));
        return probables;
    }
}
