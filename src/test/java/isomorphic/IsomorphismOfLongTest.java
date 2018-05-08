package isomorphic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class IsomorphismOfLongTest extends BaseTest {

    private static final Isomorphism<Long, Long> increment = Isomorphism.OfLong.of(
            Math::incrementExact,
            Math::decrementExact
    );

    private static final Isomorphism<Long, Long> negate = Isomorphism.OfLong.of(
            Math::negateExact,
            Math::negateExact
    );

    @Test
    void compose() {
        Isomorphism<Long, Long> composed = negate.compose(increment);
        assertEquals(-8, (long) composed.apply(7L));
    }

    @Test
    void andThen() {
        Isomorphism<Long, Long> composed = negate.andThen(increment);
        assertEquals(-6, (long) composed.apply(7L));
    }

    @Test
    void inverse() {
        Isomorphism<Long, Long> decrement = increment.inverse();
        assertEquals(7, (long) decrement.apply(increment.apply(7L)));
        assertEquals(7, (long) decrement.compose(increment).apply(7L));
        assertEquals(7, (long) decrement.andThen(increment).apply(7L));
    }

    @Test
    void identity() {
        Isomorphism<Long, Long> id = Isomorphism.identity();
        assertEquals(7, (long) id.apply(7L));
        assertEquals(7, (long) id.inverse().apply(7L));
        assertEquals(7, (long) id.compose(id).apply(7L));
        assertEquals(7, (long) id.andThen(id).apply(7L));
    }
}
