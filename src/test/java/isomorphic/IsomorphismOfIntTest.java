package isomorphic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class IsomorphismOfIntTest extends BaseTest {

    private static final Isomorphism.OfInt increment = Isomorphism.OfInt.of(
            Math::incrementExact,
            Math::decrementExact
    );

    private static final Isomorphism.OfInt negate = Isomorphism.OfInt.of(
            Math::negateExact,
            Math::negateExact
    );

    @Test
    void compose() {
        Isomorphism.OfInt composed = negate.compose(increment);
        assertEquals(-8, (int) composed.apply(7));
    }

    @Test
    void andThen() {
        Isomorphism.OfInt composed = negate.andThen(increment);
        assertEquals(-6, (int) composed.apply(7));
    }

    @Test
    void inverse() {
        Isomorphism.OfInt decrement = increment.inverse();
        assertEquals(7, (int) decrement.apply(increment.apply(7)));
        assertEquals(7, (int) decrement.compose(increment).apply(7));
        assertEquals(7, (int) decrement.andThen(increment).apply(7));
    }

    @Test
    void identity() {
        Isomorphism.OfInt id = Isomorphism.OfInt.identity();
        assertEquals(7, (int) id.apply(7));
        assertEquals(7, (int) id.inverse().apply(7));
        assertEquals(7, (int) id.compose(id).apply(7));
        assertEquals(7, (int) id.andThen(id).apply(7));
    }
}
