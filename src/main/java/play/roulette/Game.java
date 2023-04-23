package play.roulette;

import java.util.Random;

import static play.roulette.Constants.GREEN_0;
import static play.roulette.Constants.GREEN_00;
import static play.roulette.Constants.GREEN_00_NUMBER;
import static play.roulette.Constants.GREEN_0_NUMBER;
import static play.roulette.Constants.MAX_ROULETTE_NUMBER;

public class Game {
    private final Statistics stats;
    private final Random random;
    private RouletteNumber currentNumber;

    public Game(Statistics stats) {
        this.stats = stats;
        this.random = new Random();
    }

    public RouletteNumber play() {
        int outcome = random.nextInt(MAX_ROULETTE_NUMBER + 1);
        RouletteNumber outcomeRouletteNumber = null;
        if (outcome == GREEN_0 || outcome == GREEN_00) {
            outcomeRouletteNumber = outcome == GREEN_0 ? GREEN_0_NUMBER : GREEN_00_NUMBER;
        } else {
            outcomeRouletteNumber = new RouletteNumber(String.valueOf(outcome));
        }
        this.stats.updateStats(outcomeRouletteNumber);
        this.currentNumber = outcomeRouletteNumber;
        return outcomeRouletteNumber;
    }

    public RouletteNumber getCurrentNumber() {
        return currentNumber;
    }
}
