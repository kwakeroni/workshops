package be.kwakeroni.workshop.java9.exercise.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.*;

public class OptionalTest {

    /**
     * Takes an amount in a given currency and calculates the corresponding amount in euro.
     *
     * @param currency       The currency
     * @param currencyAmount The amount in the given currency
     * @return The amount in euro
     */
    private int calculateAmountInEuro(String currency, int currencyAmount) {
        ExchangeRate exchangeRate = getExchangeRate(currency).get();
        return currencyAmount * exchangeRate.rate();
    }

    /**
     * Logs the exchange rate that will be used for the given currency.
     * Logs a warning if no exchange rate is found for the currency.
     *
     * @param currency The currency
     */
    private void logExchangeRate(String currency) {
        Optional<ExchangeRate> exchangeRate = getExchangeRate(currency);
        if (exchangeRate.isPresent()) {
            logger.info("Applying exchangeRate={} for currency={}", exchangeRate.get().rate(), currency);
        } else {
            logger.warn("No exchangeRate found for currency={}", currency);
        }
    }

    /**
     * Retrieves the exchange rates for the given currencies and returns them as a Map.
     * A currency for which no exchange rate can be found will not be present in the Map.
     *
     * @param currencies The currencies
     * @return Map of currency to ExchangeRate
     */
    private Map<String, ExchangeRate> getExchangeRatesByCurrency(String... currencies) {
        return Arrays.stream(currencies)
                .map(this::getExchangeRate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(ExchangeRate::currency, Function.identity()));
    }

    /**
     * Retrieves the exchange rate for the given currency from a set of providers.
     * If the first provider does not supply an exchange rate, the second one is used, and so on.
     *
     * @param currency The currency
     * @return The exchange rate from the first successful provider
     */
    private Optional<ExchangeRate> getExchangeRate(String currency) {
        Optional<ExchangeRate> exchangeRate = ibanFirst.getExchangeRate(currency);
        if (!exchangeRate.isPresent()) {
            exchangeRate = wubs.getExchangeRate(currency);
        }
        if (!exchangeRate.isPresent()) {
            exchangeRate = ibs6Fallback.getExchangeRate(currency);
        }
        return exchangeRate;
    }

    @Test
    @DisplayName("Calculates the amount in euro based on an amount in another currency and its exchange rate")
    public void testCalculateAmount() {
        when(ibanFirst.getExchangeRate("USD")).thenReturn(Optional.of(ExchangeRate.of("USD", 2)));

        assertThat(calculateAmountInEuro("USD", 18)).isEqualTo(36);
    }

    @Test
    @DisplayName("Throws an error if the exchange rate is missing")
    public void testCalculateAmountWithMissingExchangeRate() {
        when(ibanFirst.getExchangeRate("USD")).thenReturn(Optional.of(ExchangeRate.of("USD", 2)));

        assertThatThrownBy(() -> calculateAmountInEuro("KYD", 18))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("Logs the exchange rate for a given currency")
    public void testLogExchangeRate() {
        when(ibanFirst.getExchangeRate("USD")).thenReturn(Optional.of(ExchangeRate.of("USD", 2)));

        logExchangeRate("USD");

        verify(logger).info("Applying exchangeRate={} for currency={}", 2, "USD");
    }

    @Test
    @DisplayName("Logs a warning if the exchange rate for a given currency is missing")
    public void testLogMissingExchangeRate() {
        when(ibanFirst.getExchangeRate("USD")).thenReturn(Optional.empty());

        logExchangeRate("USD");

        verify(logger).warn("No exchangeRate found for currency={}", "USD");
    }

    @Test
    @DisplayName("Retrieves a Map of exchange rates by currency")
    public void testGetExchangeRatesByCurrency() {
        when(ibanFirst.getExchangeRate("EUR")).thenReturn(Optional.of(ExchangeRate.of("EUR", 1)));
        when(ibanFirst.getExchangeRate("USD")).thenReturn(Optional.of(ExchangeRate.of("USD", 2)));
        when(ibanFirst.getExchangeRate("KYD")).thenReturn(Optional.of(ExchangeRate.of("KYD", 3)));

        assertThat(getExchangeRatesByCurrency("EUR", "USD", "KYD", "FFR"))
                .extractingFromEntries(Map.Entry::getKey, entry -> entry.getValue().rate)
                .containsExactlyInAnyOrder(tuple("EUR", 1), tuple("USD", 2), tuple("KYD", 3));
    }

    @Test
    @DisplayName("Retrieves the exchange rate from provider 1 (IbanFirst)")
    public void testGetExchangeRateFromIbanFirst() {
        when(ibanFirst.getExchangeRate(any())).thenReturn(Optional.of(ExchangeRate.of("EUR", 1)));
        when(wubs.getExchangeRate(any())).thenReturn(Optional.of(ExchangeRate.of("EUR", 2)));
        when(ibs6Fallback.getExchangeRate(any())).thenReturn(Optional.of(ExchangeRate.of("EUR", 3)));

        assertThat(getExchangeRate("EUR").get().rate()).isEqualTo(1);
    }

    @Test
    @DisplayName("Retrieves the exchange rate from provider 2 (WUBS)")
    public void testGetExchangeRateFromWubs() {
        when(ibanFirst.getExchangeRate(any())).thenReturn(Optional.empty());
        when(wubs.getExchangeRate(any())).thenReturn(Optional.of(ExchangeRate.of("EUR", 2)));
        when(ibs6Fallback.getExchangeRate(any())).thenReturn(Optional.of(ExchangeRate.of("EUR", 3)));

        assertThat(getExchangeRate("EUR").get().rate()).isEqualTo(2);
    }

    @Test
    @DisplayName("Retrieves the exchange rate from provider 3 (IBS6 Fallback)")
    public void testGetExchangeRateFromIBS6() {
        when(ibanFirst.getExchangeRate(any())).thenReturn(Optional.empty());
        when(wubs.getExchangeRate(any())).thenReturn(Optional.empty());
        when(ibs6Fallback.getExchangeRate(any())).thenReturn(Optional.of(ExchangeRate.of("EUR", 3)));

        assertThat(getExchangeRate("EUR").get().rate()).isEqualTo(3);
    }

    private static final class ExchangeRate {
        private final String currency;
        private final int rate;

        ExchangeRate(String currency, int rate) {
            this.currency = currency;
            this.rate = rate;
        }

        public int rate() {
            return this.rate;
        }

        public String currency() {
            return this.currency;
        }

        public static ExchangeRate of(String currency, int rate) {
            return new ExchangeRate(currency, rate);
        }
    }

    private interface ExchangeRateProvider {
        Optional<ExchangeRate> getExchangeRate(String currency);
    }

    private final ExchangeRateProvider ibanFirst = mock(ExchangeRateProvider.class);
    private final ExchangeRateProvider wubs = mock(ExchangeRateProvider.class);
    private final ExchangeRateProvider ibs6Fallback = mock(ExchangeRateProvider.class);
    private final Logger logger = mock(Logger.class);
}
