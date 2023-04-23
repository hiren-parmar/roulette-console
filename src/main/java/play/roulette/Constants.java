package play.roulette;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {
    public static final String EVEN_KEY = "even";
    public static final String ODD_KEY = "odd";
    public static final String BLACK_KEY = "black";
    public static final String RED_KEY = "red";
    public static final String GREEN_KEY = "green";
    public static final String ZERO_KEY = "zeroes";
    public static final String TOP_KEY = "top";
    public static final String BOTTOM_KEY = "bottom";
    public static final String LOW_KEY = "low";
    public static final String MID_KEY = "mid";
    public static final String HIGH_KEY = "high";
    public static final int MAX_ROULETTE_NUMBER = 38;
    public static final String GREEN_0_STRING = "0";
    public static final String GREEN_00_STRING = "00";
    public static final RouletteNumber GREEN_0_NUMBER = new RouletteNumber(GREEN_0_STRING);
    public static final RouletteNumber GREEN_00_NUMBER = new RouletteNumber(GREEN_00_STRING);
    public static final int GREEN_0 = 0;
    public static final int GREEN_00 = 37;
    public static final int PIVOT_MAX_LOSSES = 4;
    public static Set<String> REDS; // = new HashSet<>(
//            Arrays.asList("1", "3", "5", "7", "9", "12", "14", "16", "18", "19", "21", "23", "25", "27", "30", "32", "34", "36"));
    public static Set<String> GREENS; // = new HashSet<>(Arrays.asList("0", "00"));

    public static Set<String> BLACKS;// = Stream.iterate(1, i -> i + 1)
//            .limit(37)
//            .filter(i -> !REDS.contains(Integer.toString(i)))
//            .map(i -> Integer.toString(i))
//            .collect(Collectors.toSet());

    public static void init() {
        REDS = new HashSet<>(
                Arrays.asList("1", "3", "5", "7", "9", "12", "14", "16", "18", "19", "21", "23", "25", "27", "30", "32", "34", "36"));
        GREENS = new HashSet<>(Arrays.asList("0", "00"));
        BLACKS = Stream.iterate(1, i -> i + 1)
                .limit(37)
                .filter(i -> !REDS.contains(Integer.toString(i)))
                .map(i -> Integer.toString(i))
                .collect(Collectors.toSet());
    }

    public static Set<RouletteNumber> allRouletteNumbers() {
        Set<RouletteNumber> numbers = new LinkedHashSet<>();
        numbers.add(new RouletteNumber("0"));
        for (int i = 1; i <= 36; i++) {
            numbers.add(new RouletteNumber(String.valueOf(i)));
        }
        numbers.add(new RouletteNumber("00"));
        return numbers;
    }

    public static List<RouletteNumber> allRouletteNumbersAsList() {
        return setToList(allRouletteNumbers());
    }
    public static Set<RouletteNumber> evenOddRouletteNumbers(boolean even) {
        Set<RouletteNumber> numbers = new HashSet<>();
        int start = even ? 2 : 1;
        int end = even ? 36 : 35;
        for (int i = start; i <= end; i += 2) {
            numbers.add(new RouletteNumber(String.valueOf(i)));
        }
        return numbers;
    }

    public static Set<RouletteNumber> blackRedRouletteNumbers(boolean black) {
        Set<String> filteredNumbers = black ? BLACKS : REDS;
        return filteredNumbers.stream()
                .map(RouletteNumber::new)
                .collect(Collectors.toSet());
    }

    public static Set<RouletteNumber> topBottomRouletteNumbers(boolean top) {
        Set<RouletteNumber> numbers = new HashSet<>();
        int start = top ? 1 : 19;
        int end = top ? 18 : 36;
        for (int i = start; i <= end; i++) {
            numbers.add(new RouletteNumber(String.valueOf(i)));
        }
        return numbers;
    }

    public static Set<RouletteNumber> lowMidHighRouletteNumbers(LowMidHighRange range) {
        Set<RouletteNumber> numbers = new HashSet<>();
        int start = range.equals(LowMidHighRange.LOW) ? 1 : range.equals(LowMidHighRange.MID) ? 13 : 25;
        int end = range.equals(LowMidHighRange.LOW) ? 12 : range.equals(LowMidHighRange.MID) ? 24 : 36;
        for (int i = start; i <= end; i++) {
            numbers.add(new RouletteNumber(String.valueOf(i)));
        }
        return numbers;
    }

    public static String printRedBox(int number) {
        return "\u001B[41m " + number + " \u001B[0m";
    }

    public static String printBlackBox(int number) {
        return "\u001B[40m " + number + " \u001B[0m";
    }

    public static String printGreenBox(String number) {
       return "\u001B[42m " + number + " \u001B[0m";
    }

    public static String numberAsSubscript(String number) {
        return number.replaceAll("0", "₀")
                .replaceAll("1", "₁")
                .replaceAll("2", "₂")
                .replaceAll("3", "₃")
                .replaceAll("4", "₄")
                .replaceAll("5", "₅")
                .replaceAll("6", "₆")
                .replaceAll("7", "₇")
                .replaceAll("8", "₈")
                .replaceAll("9", "₉").toString();
    }

    public static String numberAsSuperscript(String number) {
        return number.replaceAll("0", "⁰")
                .replaceAll("1", "¹")
                .replaceAll("2", "²")
                .replaceAll("3", "³")
                .replaceAll("4", "⁴")
                .replaceAll("5", "⁵")
                .replaceAll("6", "⁶")
                .replaceAll("7", "⁷")
                .replaceAll("8", "⁸")
                .replaceAll("9", "⁹").toString();
    }

    public static List<RouletteNumber> setToList(Set<RouletteNumber> rouletteNumbers) {
        List<RouletteNumber> toList = new LinkedList<>();
        for (RouletteNumber element : rouletteNumbers) {
            toList.add(element);
        }
        return toList;
    }
}
