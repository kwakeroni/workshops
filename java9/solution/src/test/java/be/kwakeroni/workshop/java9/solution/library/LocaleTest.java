package be.kwakeroni.workshop.java9.solution.library;

import org.junit.Test;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Currency;
import java.util.Locale;

public class LocaleTest {

    @Test
    public void test() {
        test(Locale.getDefault());
        test(new Locale("nl", "BE"));
        test(new Locale("fr", "BE"));
        test(new Locale("en", "US"));
        test(new Locale("en", "GB"));
        test(new Locale("zh", "CN"));
        test(Locale.forLanguageTag("nl-GB-u-cu-EUR-fw-tue"));
    }

    public void test(Locale locale) {
        System.out.println("-- " + locale.getDisplayName(locale) + " --");
        System.out.println(locale);
        System.out.println(currency(locale));
        System.out.println(decimal(locale));
        System.out.println(dateTime(locale));
        System.out.println(week(locale));
        System.out.println();
    }

    private String currency(Locale locale) {
        Currency currency = Currency.getInstance(locale);
        return currency.getDisplayName() + " - " + currency.getCurrencyCode() + " (" + currency.getSymbol() + ")";
    }

    private String decimal(Locale locale) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        NumberFormat percentFormat = NumberFormat.getPercentInstance(locale);
        return currencyFormat.format(50) + " * " + percentFormat.format(1.21d) + " = " + currencyFormat.format(50d * 1.21d);
    }

    private String dateTime(Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).localizedBy(locale);
        return formatter.format(LocalDateTime.now());
    }

    private String week(Locale locale) {
        WeekFields weekFields = WeekFields.of(locale);
        return weekFields.getFirstDayOfWeek().getDisplayName(TextStyle.FULL, locale);

    }
}
