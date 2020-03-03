package be.kwakeroni.workshop.java9.solution.language;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionSwitch {

    @Test
    public void testShortExpression() {
        assertThat(getDayCount(Month.JANUARY, 2020)).isEqualTo(31);
        assertThat(getDayCount(Month.JUNE, 2020)).isEqualTo(30);
        assertThat(getDayCount(Month.FEBRUARY, 2020)).isEqualTo(29);
        assertThat(getDayCount(Month.FEBRUARY, 2021)).isEqualTo(28);
    }

    private static int getDayCount(Month month, int year) {
        return switch (month) {
            case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> 31;
            case APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30;
            case FEBRUARY -> (isLeapYear(year))? 29 : 28;
        };
    }

    /*
     * Expression-switch form allows to 'yield' a different value for each case.
     * It can also detect 'missing' cases for enums.
     */
    private static int dayCountWithFallthrough(Month month, int year) {
        return switch (month) {
            case JANUARY:
            case MARCH:
            case MAY:
            case JULY:
            case AUGUST:
            case OCTOBER:
            case DECEMBER:
                yield 31;
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                yield 30;
            case FEBRUARY:
                if (isLeapYear(year)) {
                    yield 29;
                } else {
                    yield 28;
                }
        };
    }

    /*
     * Comma-separated cases reduce the need for fall-through, which can make the code more readable.
     */
    private static int dayCountWithCommaCases(Month month, int year) {
        return switch (month) {
            case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER: yield 31;
            case APRIL, JUNE, SEPTEMBER, NOVEMBER: yield 30;
            case FEBRUARY:
                if (isLeapYear(year)) {
                    yield 29;
                } else {
                    yield 28;
                }
        };
    }

    /*
     * The arrow syntax can make the code more readable because it removes the need for break and yield statements.
     * - It does not allow fallthrough.
     * - It is not possible to combine colon (:) and arrow syntax in one switch.
     * -
     */
    private static int dayCountWithArrows(Month month, int year) {
        return switch (month) {
            case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> 31;
            case APRIL, JUNE, SEPTEMBER, NOVEMBER -> 30;
            case FEBRUARY -> {
                if (isLeapYear(year)) {
                    yield 29;
                } else {
                    yield 28;
                }
            }
        };
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
