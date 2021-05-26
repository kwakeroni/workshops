package be.kwakeroni.workshop.java9.solution.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The context of this test is people that have reserved a desk on a specific floor in the building.
 * The {@code ReservationService} returns a {@code Response} with the people present at a given floor.
 * The returned {@code Response} will contain a {@link Map} mapping the day of the week to the people present on that day.
 * Optionally, the {@code Response} can also contain a list of errors encountered while calling the service.
 */
public class CollectorsTest {

    public interface ReservationService {
        Response getPresenceAt(int floor);
    }

    public static record Response(Map<DayOfWeek, Set<String>> persons, List<String> errors) {
    }

    /**
     * Aggregates a stream of Responses into the Set of all persons present in those Responses.
     *
     * @param stream Stream of Responses from the ReservationService
     * @return An unmodifiable Set with person names.
     */
    public Set<String> aggregatePersons(Stream<Response> stream) {
        return stream.map(Response::persons)
                .flatMap(map -> map.values().stream())
                .flatMap(Set::stream)
                .collect(toUnmodifiableSet());
    }

    /**
     * Aggregates a stream of Responses into a Map of all person present for each day in those Responses.
     *
     * @param stream Stream of Responses from the ReservationService
     * @return A Map mapping the DayOfWeek to all persons present on that day of the week
     */
    public Map<DayOfWeek, Set<String>> aggregatePersonsByDay(Stream<Response> stream) {
        return stream.map(Response::persons)
                .flatMap(map -> map.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey,
                        mapping(Map.Entry::getValue,
                                flatMapping(Set::stream,
                                        toUnmodifiableSet()))));
    }

    /**
     * Aggregates a stream of Responses into a single Response, combining the data of all those Responses.
     *
     * @param stream Stream of Responses from the ReservationService
     * @return A single Response with all persons present by day of the week, and all errors present in the given Responses.
     */
    public Response aggregateResponses(Stream<Response> stream) {
        return stream.collect(
                Collectors.teeing(
                        aggregatingPersons(),
                        aggregatingErrors(),
                        Response::new
                ));
    }

    /**
     * @return a Collector aggregating the persons from a Response into a Map
     */
    private Collector<Response, ?, Map<DayOfWeek, Set<String>>> aggregatingPersons() {
        return aggregatingMap(Response::persons, toUnmodifiableSet());
    }

    /**
     * @return a Collector aggregating the errors from a Response into a List
     */
    private Collector<Response, ?, List<String>> aggregatingErrors() {
        return aggregating(Response::errors, toUnmodifiableList());
    }

    /**
     * Collector that aggregates Collections into a single destination collection.
     *
     * @param collectionFunction Function mapping the source type to a Collection.
     * @param downstream         Collector aggregating the Collections into a destination collection type
     * @param <T>                The source type
     * @param <E>                The type of elements in the Collections
     * @param <R>                The destination collection type of the collection aggregation
     * @return a Collector from the source type to a destination collection
     */
    private <T, E, R> Collector<T, ?, R> aggregating(Function<? super T, ? extends Collection<E>> collectionFunction, Collector<? super E, ?, R> downstream) {
        return mapping(collectionFunction,
                flatMapping(Collection<E>::stream,
                        downstream));
    }

    /**
     * Collector that aggregates Maps of collections into a Map with aggregated collections.
     *
     * @param mapFunction Function mapping the source type to a Map of collections.
     * @param downStream  Collector aggregating the mapped collections into a destination collection type
     * @param <T>         The source type
     * @param <K>         The key type in the Map
     * @param <V>         The value type in the collections in the Map
     * @param <R>         The destination collection type of the collection aggregation
     * @return a Collector from the source type to a key-to-destination Map
     */
    private <T, K, V, R> Collector<T, ?, Map<K, R>> aggregatingMap(Function<? super T, ? extends Map<K, ? extends Collection<V>>> mapFunction, Collector<? super V, ?, R> downStream) {
        return mapping(mapFunction,
                aggregating(Map::entrySet,
                        groupingBy(Map.Entry::getKey,
                                aggregating(Map.Entry::getValue, downStream))));
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
                entry(DayOfWeek.MONDAY, Set.of("David", "Stéphane", "Benedicte", "Glenn", "Gunther")),
                entry(DayOfWeek.TUESDAY, Set.of("Benedicte", "Akmal")),
                entry(DayOfWeek.WEDNESDAY, Set.of("Akmal", "Gunther"))
        );
    }

    @Test
    @DisplayName("Aggregates multiple Responses into a single Response")
    public void testAggregateResponse() {
        setupReservations();

        Response response = aggregateResponses(getPresenceInBuilding());

        assertThat(response.persons()).containsOnly(
                entry(DayOfWeek.MONDAY, Set.of("David", "Stéphane", "Benedicte", "Glenn", "Gunther")),
                entry(DayOfWeek.TUESDAY, Set.of("Benedicte", "Akmal")),
                entry(DayOfWeek.WEDNESDAY, Set.of("Akmal", "Gunther"))
        );

        assertThat(response.errors()).containsExactlyInAnyOrder(
                "ISAOffice not working on Sunday",
                "No data about Fridays",
                "No data about the kitchen"
        );
    }

    private void setupReservations() {
        when(reservationService.getPresenceAt(anyInt())).thenReturn(new Response(Map.of(), List.of()));
        when(reservationService.getPresenceAt(1)).thenReturn(
                new Response(Map.of(
                        DayOfWeek.MONDAY, Set.of("David", "Stéphane")),
                        List.of()));
        when(reservationService.getPresenceAt(2)).thenReturn(
                new Response(Map.of(
                        DayOfWeek.MONDAY, Set.of("Benedicte", "Glenn"),
                        DayOfWeek.TUESDAY, Set.of("Benedicte")),
                        List.of("ISAOffice not working on Sunday")));
        when(reservationService.getPresenceAt(6)).thenReturn(
                new Response(Map.of(
                        DayOfWeek.MONDAY, Set.of("Gunther"),
                        DayOfWeek.TUESDAY, Set.of("Akmal"),
                        DayOfWeek.WEDNESDAY, Set.of("Akmal", "Gunther")),
                        List.of("No data about Fridays", "No data about the kitchen")));
    }

    private final ReservationService reservationService = mock(ReservationService.class);
}
