package play.roulette.strategy;

import play.roulette.RouletteNumber;
import play.roulette.Statistics;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

import static play.roulette.Constants.BLACK_KEY;
import static play.roulette.Constants.BOTTOM_KEY;
import static play.roulette.Constants.EVEN_KEY;
import static play.roulette.Constants.GREEN_KEY;
import static play.roulette.Constants.ODD_KEY;
import static play.roulette.Constants.RED_KEY;
import static play.roulette.Constants.TOP_KEY;
import static play.roulette.Constants.ZERO_KEY;

public class ProbableNextOutcomeStrategy extends ProbabilisticOutcomeStrategy {
        private final int hotThreshold;
        private final int coldThreshold;

        public ProbableNextOutcomeStrategy(Statistics statistics) {
            super(statistics);
            this.hotThreshold = 10;
            this.coldThreshold = 10;
        }

        @Override
        public String getStrategyName() {
            return "Probable Next Outcome Strategy";
        }

        @Override
        public Set<RouletteNumber> getProbables() {
            ConcurrentLinkedQueue<RouletteNumber> previousOutcomes = getStatistics().getPreviousOutcomes();

            Map<RouletteNumber, Long> numberFrequencies = previousOutcomes.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            Map<String, Map<String, Long>> categoryFrequencies = new ConcurrentHashMap<>();

            // Calculate frequencies of red/black, even/odd, high/low
            categoryFrequencies.put("color", previousOutcomes.stream()
                    .collect(Collectors.groupingBy(number -> {
                        if (number.isGreen()) return GREEN_KEY;
                        else if (number.isRed()) return RED_KEY;
                        else return BLACK_KEY;
                    }, Collectors.counting())));

            categoryFrequencies.put("parity", previousOutcomes.stream()
                    .collect(Collectors.groupingBy(number -> {
                        if (number.isZero()) return ZERO_KEY;
                        else if (number.isEven()) return EVEN_KEY;
                        else return ODD_KEY;
                    }, Collectors.counting())));

            categoryFrequencies.put("range", previousOutcomes.stream()
                    .collect(Collectors.groupingBy(number -> {
                        if (number.isZero()) return ZERO_KEY;
                        else if (number.isTop()) return TOP_KEY;
                        else return BOTTOM_KEY;
                    }, Collectors.counting())));

            Set<RouletteNumber> hotNumbers = numberFrequencies.entrySet().stream()
                    .filter(entry -> entry.getValue() >= hotThreshold)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            Set<RouletteNumber> coldNumbers = numberFrequencies.entrySet().stream()
                    .filter(entry -> entry.getValue() <= coldThreshold)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            Map<String, RouletteNumber> mostFrequentNumbersInEachCategory = new HashMap<>();

            for (Map.Entry<String, Map<String, Long>> categoryFrequency : categoryFrequencies.entrySet()) {
                Map<String, Long> frequencyMap = categoryFrequency.getValue();
                Optional<Map.Entry<String, Long>> maxEntry = frequencyMap.entrySet().stream()
                        .max(Map.Entry.comparingByValue());

                maxEntry.ifPresent(entry -> mostFrequentNumbersInEachCategory.put(categoryFrequency.getKey(),
                        new RouletteNumber(entry.getKey())));
            }

            // Calculate probability of the next outcome
            Map<RouletteNumber, Double> outcomeProbabilities = new ConcurrentHashMap<>();

            for (int i = 0; i <= 36; i++) {
                RouletteNumber number = new RouletteNumber(String.format("%02d", i));

                double probability = 0.0;

                if (hotNumbers.contains(number)) {
                    probability += 0.3;
                }

                if (coldNumbers.contains(number)) {
                    probability -= 0.3;
                }

                if (mostFrequentNumbersInEachCategory.containsValue(number)) {
                    probability += 0.2;
                }

//                if (mostFrequentNumbersInEachCategory.get("color").equals(number.getColorAsString())) {
//                    probability += 0.1;
//                }

                outcomeProbabilities.put(number, probability);
            }

            // Sort the outcomes by probability
            Set<RouletteNumber> probableOutcomes = outcomeProbabilities.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            return probableOutcomes;


            // Return the top probable outcomes
//            return probableOutcomes.subList(0, Math.min(probableOutcomes.size(), 5));
        }
}

