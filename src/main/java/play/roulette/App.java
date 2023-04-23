package play.roulette;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import play.roulette.strategy.HotNumbersStrategy;
import play.roulette.strategy.LawOfLargeNumbersStrategy;
import play.roulette.strategy.LawOfThirdsStrategy;
import play.roulette.strategy.OutcomeProbabilityStrategy;
import play.roulette.strategy.PivotStrategy;
import play.roulette.strategy.ProbabilisticOutcomeStrategy;
import play.roulette.strategy.ProbableNextOutcomeStrategy;
import play.roulette.strategy.RecentBlackRedStrategy;
import play.roulette.strategy.RecentEvenOddStrategy;
import play.roulette.strategy.RecentLowMidHighStrategy;
import play.roulette.strategy.RecentTopBottomStrategy;


public class App {

    public static final int SIMULATION_ROUNDS = 1_000_000;

    public static void main(String[] args) {
        Constants.init();
        Statistics statistics = new Statistics(SIMULATION_ROUNDS, 10);
        Game game = new Game(statistics);
        Set<ProbabilisticOutcomeStrategy> predictingStrategies = new HashSet<>();
        Map<ProbabilisticOutcomeStrategy, Integer> strategyPoints = new HashMap<>();
        predictingStrategies.add(new RecentBlackRedStrategy(statistics));
        predictingStrategies.add(new RecentEvenOddStrategy(statistics));
        predictingStrategies.add(new RecentTopBottomStrategy(statistics));
        predictingStrategies.add(new RecentLowMidHighStrategy(statistics));
        predictingStrategies.add(new OutcomeProbabilityStrategy(statistics));
        predictingStrategies.add(new LawOfLargeNumbersStrategy(statistics));
        predictingStrategies.add(new LawOfThirdsStrategy(statistics));
        predictingStrategies.add(new HotNumbersStrategy(statistics));
        predictingStrategies.add(new PivotStrategy(statistics));
        //        predictingStrategies.add(new ProbableNextOutcomeStrategy(statistics));

        // Dynamically load all implementations of PredictingStrategy
        for (ProbabilisticOutcomeStrategy strategy :
                predictingStrategies) {
            strategyPoints.put(strategy, 0);
        }

        Scanner scanner = new Scanner(System.in);
        String input = "";
        System.out.print("Start Game?: y/N \n");
        input = scanner.nextLine();
        if (input.equals("N")) {
            System.out.println("Well, See you next time");
        }

        int i = 0;
        while (input.equals("y")) {
            RouletteNumber outcome = game.play();
            System.out.println(outcome);

            //            for (ProbabilisticOutcomeStrategy strategy : predictingStrategies) {
            //                System.out.println(strategy);
            //            }

            if (i < SIMULATION_ROUNDS) {
                input = "y";
            } else {
                System.out.print("Play Next Round?: y/N \n s for Statistics \n");
                input = scanner.nextLine();
            }
            if (input.equals("s")) {
                //System.out.println(statistics.getPreviousOutcomes().stream().map(RouletteNumber::getBoxedNumber).collect(Collectors.joining(", ")));
                input = "y";
            }
            if (input.equals("N")) {
                System.out.println("Well, See you next time");
                String whoWins = strategyPoints.keySet().stream()
                        .map(strategy -> strategy.getStrategyName() + " got " + strategyPoints.get(strategy) + "/" + SIMULATION_ROUNDS)
                        .collect(Collectors.joining("\n"));
                System.out.println(whoWins);
                System.exit(0);
            }
            for (ProbabilisticOutcomeStrategy strategy : predictingStrategies) {
                if (strategy == null || strategy.getProbables() == null) continue;
                if (strategy.getProbables().contains(outcome)) {
                    strategyPoints.put(strategy, strategyPoints.get(strategy) + 1);
                }
            }
            i++;
        }
    }
}
