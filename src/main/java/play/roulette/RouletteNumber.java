package play.roulette;

import java.util.Objects;

import static play.roulette.Constants.BLACK_KEY;
import static play.roulette.Constants.GREENS;
import static play.roulette.Constants.GREEN_0;
import static play.roulette.Constants.GREEN_00;
import static play.roulette.Constants.GREEN_00_STRING;
import static play.roulette.Constants.GREEN_0_STRING;
import static play.roulette.Constants.GREEN_KEY;
import static play.roulette.Constants.REDS;
import static play.roulette.Constants.RED_KEY;
import static play.roulette.Constants.init;
import static play.roulette.Constants.printBlackBox;
import static play.roulette.Constants.printGreenBox;
import static play.roulette.Constants.printRedBox;

public class RouletteNumber implements Comparable<RouletteNumber> {
    private final String number;
    private final String boxedNumber;
    private final int numeric;
    private final boolean even;
    private final boolean red;
    private final boolean green;
    private final boolean low;
    private final boolean mid;
    private final boolean high;
    private final boolean top;
    private final boolean bottom;
    private final boolean zero;
    private double probability;

    private String color;

    public RouletteNumber(String number) {
        int intNumber;
        intNumber = Integer.parseInt(number);
        if (number.equals(GREEN_0_STRING)) {
            intNumber = GREEN_0;
        }
        if (number.equals(GREEN_00_STRING)) {
            intNumber = GREEN_00;
        }
        this.numeric = intNumber;
        this.number = number;
        this.even = (intNumber % 2 == 0);
        init();
        this.red = REDS.contains(number);
        this.green = GREENS.contains(number);
        if (this.green) {
            this.boxedNumber = printGreenBox(number);
        } else if (this.red) {
            this.boxedNumber = printRedBox(intNumber);
        } else {
            this.boxedNumber = printBlackBox(intNumber);
        }
        this.top = intNumber < 19;
        this.bottom = intNumber >= 19;
        this.zero = green;
        this.low = intNumber < 12;
        this.mid = intNumber >= 12 && intNumber < 24;
        this.high = intNumber >= 24;
    }

    public String getNumber() {
        return this.number;
    }

    public String getBoxedNumber() {
        return boxedNumber;
    }

    public boolean isEven() {
        return this.even;
    }

    public boolean isRed() {
        return this.red;
    }

    public boolean isGreen() {
        return this.green;
    }

    public boolean isLow() {
        return low;
    }

    public boolean isMid() {
        return mid;
    }

    public boolean isHigh() {
        return high;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isBottom() {
        return bottom;
    }

    public boolean isZero() {
        return zero;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The number is ").append(boxedNumber);
        sb.append(", an ");
        if (even) {
            sb.append("Even");
        } else {
            sb.append("Odd");
        }
        sb.append(" number, falls in ");
        if (top) {
            sb.append("Top 18");
        } else {
            sb.append("Bottom 18");
        }
        sb.append(", also falls in ");
        if (low) {
            sb.append("Low 12");
        } else if (mid) {
            sb.append("Mid 12");
        } else {
            sb.append("High 12");
        }
        return sb.toString();
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getNumeric() {
        return numeric;
    }
    public String getColor() {
        if (this.isGreen()) {
            this.color = GREEN_KEY;
        }
        if (this.isRed()) {
            this.color = RED_KEY;
        }
        if (this.color == null || this.color.equals("")) {
            this.color = BLACK_KEY;
        }
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouletteNumber that = (RouletteNumber) o;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public int compareTo(RouletteNumber number) {
        return number.getNumber().compareTo(this.getNumber());
    }
}

