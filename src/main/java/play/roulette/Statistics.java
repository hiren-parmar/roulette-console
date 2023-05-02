package play.roulette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import static play.roulette.Constants.BLACK_KEY;
import static play.roulette.Constants.BOTTOM_KEY;
import static play.roulette.Constants.EVEN_KEY;
import static play.roulette.Constants.GREEN_KEY;
import static play.roulette.Constants.HIGH_KEY;
import static play.roulette.Constants.LOW_KEY;
import static play.roulette.Constants.MID_KEY;
import static play.roulette.Constants.ODD_KEY;
import static play.roulette.Constants.RED_KEY;
import static play.roulette.Constants.TOP_KEY;
import static play.roulette.Constants.ZERO_KEY;

public class Statistics {
    // Properties
    private int numberOfGames; // number of previous games to track
    private int numberOfHotColdNumbers;
    private Map<RouletteNumber, Integer> previousOutcomesWithCount; // list of previous game outcomes
    private ConcurrentLinkedQueue<RouletteNumber> previousOutcomes; // list of previous game outcomes
    private Map<String, Integer> evenOddStats; // count of even/odd outcomes
    private Map<String, Integer> blackRedStats; // count of black/red outcomes
    private Map<String, Integer> topBottomStats; // count of top 18/bottom 18/middle outcomes
    private Map<String, Integer> lowMidHighStats; // count of low 12/mid 12/high 12 outcomes
    private List<RouletteNumber> hotNumbers; // top numberOfHotColdNumbers numbers that have occurred most frequently
    private List<RouletteNumber> coldNumbers; // bottom numberOfHotColdNumbers numbers that have occurred least frequently

    // Constructor
    public Statistics(int numberOfGames, int numberOfHotColdNumbers) {
        this.numberOfGames = numberOfGames;
        this.numberOfHotColdNumbers = numberOfHotColdNumbers;
        this.previousOutcomesWithCount = new LinkedHashMap<>();
        this.previousOutcomes = new ConcurrentLinkedQueue<>();
        this.evenOddStats = new HashMap<>();
        this.blackRedStats = new HashMap<>();
        this.topBottomStats = new HashMap<>();
        this.lowMidHighStats = new HashMap<>();
        this.hotNumbers = new ArrayList<>();
        this.coldNumbers = new ArrayList<>();
    }

    // Getters
    public int getNumberOfGames() {
        return this.numberOfGames;
    }

    public int getNumberOfHotColdNumbers() {
        return numberOfHotColdNumbers;
    }

    public Map<RouletteNumber, Integer> getPreviousOutcomesWithCount() {
        return this.previousOutcomesWithCount;
    }

    public ConcurrentLinkedQueue<RouletteNumber> getPreviousOutcomes() {
        return previousOutcomes;
    }

    public Map<String, Integer> getEvenOddStats() {
        return this.evenOddStats;
    }

    public Map<String, Integer> getBlackRedStats() {
        return this.blackRedStats;
    }

    public Map<String, Integer> getTopBottomStats() {
        return this.topBottomStats;
    }

    public Map<String, Integer> getLowMidHighStats() {
        return this.lowMidHighStats;
    }

    public List<RouletteNumber> getHotNumbers() {
        return this.hotNumbers;
    }

    public List<RouletteNumber> getColdNumbers() {
        return this.coldNumbers;
    }


    // Update the statistics based on a new outcome
    public void updateStats(RouletteNumber outcome) {
        // Add the outcome to the list of previous outcomes
        this.previousOutcomesWithCount.put(outcome, this.previousOutcomesWithCount.getOrDefault(outcome, 0) + 1);
        this.previousOutcomes.add(outcome);

        // Update the various statistics
        updateEvenOddStats(outcome);
        updateBlackRedStats(outcome);
        updateTopBottomStats(outcome);
        updateLowMidHighStats(outcome);
        updateHotNumbers();
        updateColdNumbers();
    }

    // Update the count of even/odd outcomes
    private void updateEvenOddStats(RouletteNumber outcome) {
        String key = outcome.isEven() ? EVEN_KEY : ODD_KEY;
        if (evenOddStats.containsKey(key)) {
            evenOddStats.put(key, evenOddStats.get(key) + 1);
        } else {
            evenOddStats.put(key, 1);
        }
    }

    // Update the count of black/red outcomes
    private void updateBlackRedStats(RouletteNumber outcome) {
        String key = outcome.isGreen() ? GREEN_KEY : outcome.isRed() ? RED_KEY : BLACK_KEY;
        if (this.blackRedStats.containsKey(key)) {
            this.blackRedStats.put(key, this.blackRedStats.get(key) + 1);
        } else {
            this.blackRedStats.put(key, 1);
        }
    }

    // Update the count of top 18/bottom 18/middle outcomes
    private void updateTopBottomStats(RouletteNumber outcome) {
        String key = outcome.isZero() ? ZERO_KEY : "";
        if (key.equals("")) {
            key = outcome.isTop() ? TOP_KEY : BOTTOM_KEY;
        }
        if (this.topBottomStats.containsKey(key)) {
            this.topBottomStats.put(key, this.topBottomStats.get(key) + 1);
        } else {
            this.topBottomStats.put(key, 1);
        }
    }

    // Update the count of low 12/mid 12/high 12 outcomes
    private void updateLowMidHighStats(RouletteNumber outcome) {
        String key = outcome.isZero() ? ZERO_KEY : "";
        if (key.equals("")) {
            key = outcome.isTop() ? LOW_KEY : outcome.isMid() ? MID_KEY : HIGH_KEY;
        }
        if (this.lowMidHighStats.containsKey(key)) {
            this.lowMidHighStats.put(key, this.lowMidHighStats.get(key) + 1);
        } else {
            this.lowMidHighStats.put(key, 1);
        }
    }

    // Update the list of hot numbers (top 10 numbers that have occurred most frequently)
    private void updateHotNumbers() {
        // Sort the map by count, descending
        List<Map.Entry<RouletteNumber, Integer>> sortedEntries = new ArrayList<>(this.previousOutcomesWithCount.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        // Update the hot numbers list
        this.hotNumbers.clear();
        for (int i = 0; i < numberOfHotColdNumbers && i < sortedEntries.size(); i++) {
            this.hotNumbers.add(sortedEntries.get(i).getKey());
        }
    }

    // Update the list of cold numbers (bottom 10 numbers that have occurred least frequently)
    private void updateColdNumbers() {
        // Sort the map by count, ascending
        List<Map.Entry<RouletteNumber, Integer>> sortedEntries = new ArrayList<>(previousOutcomesWithCount.entrySet());
        sortedEntries.sort((e1, e2) -> e1.getValue().compareTo(e2.getValue()));
        // Update the cold numbers list
        this.coldNumbers.clear();
        for (int i = 0; i < numberOfHotColdNumbers && i < sortedEntries.size(); i++) {
            this.coldNumbers.add(sortedEntries.get(i).getKey());
        }
    }
}


