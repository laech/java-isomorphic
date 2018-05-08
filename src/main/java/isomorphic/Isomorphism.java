package isomorphic;

import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 * A function paired with an {@link #inverse() inverse}.
 * <pre>
 * Isomorphism&lt;Integer, String&gt; f = ...
 * Isomorphism&lt;String, Integer&gt; g = f.inverse();
 * g.apply(f.apply(x)) == x == f.apply(g.apply(x));
 * </pre>
 */
public interface Isomorphism<A, B> extends Function<A, B> {

    /**
     * Returns the inverse function.
     */
    Isomorphism<B, A> inverse();

    /**
     * Creates an isomorphism from a pair of functions,
     * where {@code f} is the {@link #apply(Object) apply} function,
     * and {@code g} is the {@link #inverse() inverse} function.
     *
     * @throws NullPointerException if {@code f} or {@code g} is null
     */
    static <A, B> Isomorphism<A, B> of(Function<A, B> f, Function<B, A> g) {
        return new Isomorphism<A, B>() {

            @Override
            public B apply(A a) {
                return f.apply(a);
            }

            @Override
            public Isomorphism<B, A> inverse() {
                return of(g, f);
            }
        };
    }

    /**
     * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     *
     * @throws NullPointerException if {@code before} is null
     */
    default <A0> Isomorphism<A0, B> compose(Isomorphism<A0, A> before) {
        return of(
                compose((Function<A0, A>) before),
                before.inverse().compose((Function<B, A>) inverse())
        );
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     *
     * @throws NullPointerException if {@code after} is null
     */
    default <C> Isomorphism<A, C> andThen(Isomorphism<B, C> after) {
        return of(
                andThen((Function<B, C>) after),
                after.inverse().andThen((Function<B, A>) inverse())
        );
    }

    /**
     * Returns a function that always returns its input argument.
     */
    static <A> Isomorphism<A, A> identity() {
        return of(Function.identity(), Function.identity());
    }


    /**
     * An isomorphism specialised for {@code int} values.
     */
    interface OfInt extends Isomorphism<Integer, Integer>, IntUnaryOperator {

        @Override
        OfInt inverse();

        @Override
        default Integer apply(Integer value) {
            return applyAsInt(value);
        }

        /**
         * Returns a composed function that first applies the {@code before}
         * function to its input, and then applies this function to the result.
         *
         * @throws NullPointerException if {@code before} is null
         */
        default OfInt compose(OfInt before) {
            requireNonNull(before);
            return OfInt.of(
                    x -> applyAsInt(before.applyAsInt(x)),
                    y -> before.applyAsInt(applyAsInt(y))
            );
        }

        /**
         * Returns a composed function that first applies this function to
         * its input, and then applies the {@code after} function to the result.
         *
         * @throws NullPointerException if {@code after} is null
         */
        default OfInt andThen(OfInt after) {
            requireNonNull(after);
            return OfInt.of(
                    x -> after.applyAsInt(applyAsInt(x)),
                    y -> applyAsInt(after.applyAsInt(y))
            );
        }

        /**
         * Returns a function that always returns its input argument.
         */
        static OfInt identity() {
            return OfInt.of(
                    IntUnaryOperator.identity(),
                    IntUnaryOperator.identity()
            );
        }

        /**
         * Creates an isomorphism from a pair functions,
         * where {@code f} is the {@link OfInt#applyAsInt(int) apply} function,
         * and {@code g} is the {@link OfInt#inverse() inverse} function.
         *
         * @throws NullPointerException if {@code f} or {@code g} is null
         */
        static OfInt of(IntUnaryOperator f, IntUnaryOperator g) {
            requireNonNull(f);
            requireNonNull(g);
            return new OfInt() {

                @Override
                public int applyAsInt(int value) {
                    return f.applyAsInt(value);
                }

                @Override
                public OfInt inverse() {
                    return OfInt.of(g, f);
                }
            };
        }

    }

    /**
     * An isomorphism specialised for {@code long} values.
     */
    interface OfLong extends Isomorphism<Long, Long>, LongUnaryOperator {

        @Override
        OfLong inverse();

        @Override
        default Long apply(Long value) {
            return applyAsLong(value);
        }

        /**
         * Returns a composed function that first applies the {@code before}
         * function to its input, and then applies this function to the result.
         *
         * @throws NullPointerException if {@code before} is null
         */
        default OfLong compose(OfLong before) {
            requireNonNull(before);
            return OfLong.of(
                    x -> applyAsLong(before.applyAsLong(x)),
                    y -> before.applyAsLong(applyAsLong(y))
            );
        }

        /**
         * Returns a composed function that first applies this function to
         * its input, and then applies the {@code after} function to the result.
         *
         * @throws NullPointerException if {@code after} is null
         */
        default OfLong andThen(OfLong after) {
            requireNonNull(after);
            return OfLong.of(
                    x -> after.applyAsLong(applyAsLong(x)),
                    y -> applyAsLong(after.applyAsLong(y))
            );
        }

        /**
         * Returns a function that always returns its input argument.
         */
        static OfLong identity() {
            return OfLong.of(
                    LongUnaryOperator.identity(),
                    LongUnaryOperator.identity()
            );
        }

        /**
         * Creates an isomorphism from a pair functions,
         * where {@code f} is the {@link OfLong#apply(Object) apply} function,
         * and {@code g} is the {@link OfLong#inverse() inverse} function.
         *
         * @throws NullPointerException if {@code f} or {@code g} is null
         */
        static OfLong of(LongUnaryOperator f, LongUnaryOperator g) {
            requireNonNull(f);
            requireNonNull(g);
            return new OfLong() {

                @Override
                public long applyAsLong(long value) {
                    return f.applyAsLong(value);
                }

                @Override
                public OfLong inverse() {
                    return OfLong.of(g, f);
                }
            };
        }
    }
}
