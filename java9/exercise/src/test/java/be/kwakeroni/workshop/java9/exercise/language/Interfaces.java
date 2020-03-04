package be.kwakeroni.workshop.java9.exercise.language;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class Interfaces {

    /*
     * The Entity class has an "id" and a "name".
     */
    public final class Entity {
        private final Long id;
        private final String name;

        public Entity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return this.name;
        }
    }

    /*
     * The Registry interface specifies how to retrieve an "id" from any type T.
     * It also specifies some default convenience methods.
     */
    public interface Registry<T> {

        public Long getId(T t);

        public default Map<Long, T> byId(Collection<T> collection) {
            Map<Long, T> map = new HashMap<>();
            for (T t : collection) {
                map.put(getId(t), t);
            }
            return map;
        }

        public default Map<Long, String> asStringById(Collection<T> collection) {
            Map<Long, String> map = new HashMap<>();
            for (T t : collection) {
                map.put(getId(t), t.toString());
            }
            return map;
        }

        public default <K> Map<K, T> by(Function<T, K> keyMapper, Collection<T> collection) {
            Map<K, T> map = new HashMap<>();
            for (T t : collection) {
                map.put(keyMapper.apply(t), t);
            }
            return map;
        }

        public default <K> Map<K, Long> asIdBy(Function<T, K> keyMapper, Collection<T> collection) {
            Map<K, Long> map = new HashMap<>();
            for (T t : collection) {
                map.put(keyMapper.apply(t), getId(t));
            }
            return map;
        }
    }

    /*
     * The Registry interface below provides default convenience methods
     * to create an id-to-value map from a list of entities.
     *
     * Refactor the interface to remove the duplicated code from these two methods.
     * Hint: use a Function<T, V> valueMapper to transform the entity T to a value V.
     */
    @Test
    public void test1() {
        Entity entity1 = new Entity(1L, "one");
        Entity entity2 = new Entity(2L, "two");
        List<Entity> entities = Arrays.asList(entity1, entity2);

        Registry<Entity> registry = Entity::getId;

        Map<Long, Entity> entityMap = registry.byId(entities);
        Map<Long, String> stringMap = registry.asStringById(entities);

        assertThat(entityMap.get(1L)).isEqualTo(entity1);
        assertThat(stringMap.get(2L)).isEqualTo("two");
    }

    /*
     * The Registry interface also provides convenience methods
     * to create key-to-entity and key-to-id maps.
     *
     * Refactor the interface to remove the duplicated code from these methods.
     */
    @Test
    public void test2() {
        Entity entity1 = new Entity(1L, "one");
        Entity entity2 = new Entity(2L, "two");
        List<Entity> entities = Arrays.asList(entity1, entity2);

        Registry<Entity> registry = Entity::getId;

        Map<String, Entity> entityMap = registry.by(Entity::getName, entities);
        Map<String, Long> idMap = registry.asIdBy(Entity::getName, entities);

        assertThat(entityMap.get("two")).isEqualTo(entity2);
        assertThat(idMap.get("one")).isEqualTo(1L);
    }

}
