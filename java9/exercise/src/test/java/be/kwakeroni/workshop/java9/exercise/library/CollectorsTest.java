package be.kwakeroni.workshop.java9.exercise.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.MapEntry.entry;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The context of this test is people that have reserved a desk on a specific floor in the building.
 * The {@code ReservationService} returns a {@code Response} with the people present at a given floor:
 * <p><img src="collectors-service.png" /></p>
 * The returned {@code Response} will contain a {@link Map} mapping the day of the week to the people present on that day.
 * Optionally, the {@code Response} can also contain a list of errors encountered while calling the service.
 * <p><img src="collectors-response.png" /></p>
 * The exercises will focus on <em>aggregating</em> the data of multiple Responses into another data structure.
 * The exercises will use a fixed set of test data, in the form of 3 responses, as illustrated below.
 * <p><img src="collectors-test-data.png" /></p>
 */
public class CollectorsTest {

    public interface ReservationService {
        Response getPresenceAt(int floor);
    }

    public static final class Response {
        private final Map<DayOfWeek, Set<String>> persons;

        private final List<String> errors;

        public Response(Map<DayOfWeek, Set<String>> persons, List<String> errors) {
            this.persons = persons;
            this.errors = errors;
        }

        public Map<DayOfWeek, Set<String>> persons() {
            return persons;
        }

        public List<String> errors() {
            return errors;
        }
    }

    /**
     * Aggregates a stream of Responses into the Set of all persons present in those Responses.
     * An example for the test data is given in the following image:
     * <p><img src="collectors-persons.png" /></p>
     *
     * @param stream Stream of Responses from the ReservationService
     * @return An unmodifiable Set with person names.
     */
    public Set<String> aggregatePersons(Stream<Response> stream) {
        return stream.map(Response::persons)
                .flatMap(map -> map.values().stream())
                .flatMap(Set::stream)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    /**
     * Aggregates a stream of Responses into a Map of all person present for each day in those Responses.
     * An example for the test data is given in the following image:
     * <p><img src="collectors-persons-by-day.png" /></p>
     *
     * @param stream Stream of Responses from the ReservationService
     * @return A Map mapping the DayOfWeek to all persons present on that day of the week
     */
    public Map<DayOfWeek, Set<String>> aggregatePersonsByDay(Stream<Response> stream) {
        Map<DayOfWeek, List<Set<String>>> floorSetsByDay = stream.map(Response::persons)
                .flatMap(map -> map.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toList())));

        return floorSetsByDay.entrySet().stream()
                .flatMap(this::entryStream)
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())));
    }

    private Stream<Map.Entry<DayOfWeek, String>> entryStream(Map.Entry<DayOfWeek, List<Set<String>>> entry) {
        DayOfWeek day = entry.getKey();

        return entry.getValue()
                .stream()
                .flatMap(Set::stream)
                .map(person -> entry(day, person));
    }

    /**
     * Aggregates a stream of Responses into a single Response, combining the data of all those Responses.
     * An example for the test data is given in the following image:
     * <p><img src="collectors-response-aggregate.png" /></p>
     *
     * @param stream Stream of Responses from the ReservationService
     * @return A single Response with all persons present by day of the week, and all errors present in the given Responses.
     */
    public Response aggregateResponses(Stream<Response> stream) {
        List<Response> responses = stream.collect(toList());
        Map<DayOfWeek, Set<String>> persons = aggregatePersonsByDay(responses.stream());
        List<String> errors = responses.stream().map(Response::errors).flatMap(List::stream).collect(toList());
        return new Response(persons, errors);
    }

    /**
     * @return a Stream of presence Responses for each floor in the building
     */
    private Stream<Response> getPresenceInBuilding() {
        return IntStream.rangeClosed(0, 6).mapToObj(reservationService::getPresenceAt);

    }

    @Test
    @DisplayName("Aggregates the users from Responses into an unmodifiable Set")
    public void testAggregateUsers() {
        setupReservations();

        Set<String> persons = aggregatePersons(getPresenceInBuilding());

        assertThat(persons).containsExactlyInAnyOrder("David", "Stéphane", "Benedicte", "Glenn", "Gunther", "Akmal");
        assertThatThrownBy(() -> persons.add("Geraldine"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Aggregates the users from Responses into a Map, grouped by week day")
    public void testAggregatePersonsByDay() {
        setupReservations();

        Map<DayOfWeek, Set<String>> personsByDay = aggregatePersonsByDay(getPresenceInBuilding());

        assertThat(personsByDay).containsOnly(
                entry(DayOfWeek.MONDAY, setOf("David", "Stéphane", "Benedicte", "Glenn", "Gunther")),
                entry(DayOfWeek.TUESDAY, setOf("Benedicte", "Akmal")),
                entry(DayOfWeek.WEDNESDAY, setOf("Akmal", "Gunther"))
        );
    }

    @Test
    @DisplayName("Aggregates multiple Responses into a single Response")
    public void testAggregateResponse() {
        setupReservations();

        Response response = aggregateResponses(getPresenceInBuilding());

        assertThat(response.persons()).containsOnly(
                entry(DayOfWeek.MONDAY, setOf("David", "Stéphane", "Benedicte", "Glenn", "Gunther")),
                entry(DayOfWeek.TUESDAY, setOf("Benedicte", "Akmal")),
                entry(DayOfWeek.WEDNESDAY, setOf("Akmal", "Gunther"))
        );

        assertThat(response.errors()).containsExactlyInAnyOrder(
                "ISAOffice not working on Sunday",
                "No data about Fridays",
                "No data about the kitchen"
        );
    }

    private void setupReservations() {
        when(reservationService.getPresenceAt(anyInt())).thenReturn(new Response(emptyMap(), emptyList()));
        when(reservationService.getPresenceAt(1)).thenReturn(
                new Response(
                        singletonMap(DayOfWeek.MONDAY, setOf("David", "Stéphane")),
                        emptyList()));
        when(reservationService.getPresenceAt(2)).thenReturn(
                new Response(mapOf(
                        DayOfWeek.MONDAY, setOf("Benedicte", "Glenn"),
                        DayOfWeek.TUESDAY, setOf("Benedicte")),
                        singletonList("ISAOffice not working on Sunday")));
        when(reservationService.getPresenceAt(6)).thenReturn(
                new Response(mapOf(
                        DayOfWeek.MONDAY, setOf("Gunther"),
                        DayOfWeek.TUESDAY, setOf("Akmal"),
                        DayOfWeek.WEDNESDAY, setOf("Akmal", "Gunther")),
                        Arrays.asList("No data about Fridays", "No data about the kitchen")));
    }

    @SafeVarargs
    private final <T> Set<T> setOf(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    private <K, V> Map<K, V> mapOf(K key1, V value1, K key2, V value2) {
        Map<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    private <K, V> Map<K, V> mapOf(K key1, V value1, K key2, V value2, K key3, V value3) {
        Map<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        return map;
    }

    private final ReservationService reservationService = mock(ReservationService.class);
}
