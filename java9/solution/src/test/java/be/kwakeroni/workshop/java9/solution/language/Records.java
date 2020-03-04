package be.kwakeroni.workshop.java9.solution.language;

import org.junit.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @since 14 (preview)
 */
public class Records {

    /**
     * Records cannot extend any other class
     * Records are implicitly final and cannot be abstract
     * Records cannot declare additional instance fields
     * Instance fields of records are implicitly final
     */
    public record IntRange(int from, int to) {

        // The "canonical" constructor can be expressed in "compact" form
        public IntRange {
            if (from > to) {
                throw new IllegalArgumentException("from > to");
            }
        }

        public IntRange(int singleUnit) {
            this(singleUnit, singleUnit);
        }

        public IntRange() {
            this(0, 0);
        }

        // It is allowed to override accessor methods
        public int from() {
            return Math.max(0, this.from);
        }

        public boolean overlaps(IntRange other) {
            return (from >= other.from && from <= other.to)
                   || (to <= other.to && to >= other.from)
                   || (other.from >= from && other.to <= to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    @Test
    public void testAccessors() {
        IntRange range = new IntRange(1, 2);
        assertThat(range.from()).isEqualTo(1);
        assertThat(range.to()).isEqualTo(2);
    }

    @Test
    public void testEqualsHashCode() {
        IntRange range1 = new IntRange(1, 2);
        IntRange range2 = new IntRange(1, 2);
        IntRange range3 = new IntRange(1, 3);

        assertThat(range1).isNotSameAs(range2);
        assertThat(range1).isEqualTo(range2);
        assertThat(range1).isNotEqualTo(range3);
        assertThat(range1.hashCode()).isEqualTo(range2.hashCode());
    }

    @Test
    public void testToString() {
        IntRange range = new IntRange(1, 2);
        System.out.println(range.toString());
        assertThat(range.toString())
                .contains("IntRange")
                .containsPattern("from.1")
                .containsPattern("to.2");
    }

    @Test
    public void testCustomMethods() {
        IntRange range1 = new IntRange(1, 7);
        IntRange range2 = new IntRange(2, 4);
        IntRange range3 = new IntRange(5, 9);

        assertThat(range1.overlaps(range2)).isTrue();
        assertThat(range1.overlaps(range3)).isTrue();
        assertThat(range2.overlaps(range3)).isFalse();
    }

    @Test
    public void testConstructorValidation() {
        assertThatThrownBy(() -> new IntRange(2, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
