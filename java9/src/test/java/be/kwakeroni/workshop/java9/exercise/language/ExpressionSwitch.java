package be.kwakeroni.workshop.java9.exercise.language;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Switch statements can be awkward to write and hard to read.
 * Fallthrough and the need for the break statement can make for easy mistakes.
 * Since Java 14 switch can be used as an expression (previewed in 12 and 13).
 * Together with this change came some syntactical improvements.
 */
public class ExpressionSwitch {

    @Test
    public void testShortExpression() {
        assertThat(getDayCount(Month.JANUARY, 2020)).isEqualTo(31);
        assertThat(getDayCount(Month.JUNE, 2020)).isEqualTo(30);
        assertThat(getDayCount(Month.FEBRUARY, 2020)).isEqualTo(29);
        assertThat(getDayCount(Month.FEBRUARY, 2021)).isEqualTo(28);
    }

    // Use the switch expression and the new syntax support to improve the following method.
    private static int getDayCount(Month month, int year) {
        int days = 0;
        switch (month) {
            case JANUARY:
            case MARCH:
            case JULY:
            case AUGUST:
            case OCTOBER:
            case DECEMBER:
                days = 31;
                break;
            case APRIL:
            case JUNE:
            case NOVEMBER:
            case SEPTEMBER:
                days = 30;
                break;
            case FEBRUARY:
                if (isLeapYear(year)) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
        }
        return days;
    }

    private static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0;
    }

    public enum Month {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER;
    }
}
