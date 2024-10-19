package org.saltations.systematics.core;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The `Outcome` Monad is a concept in functional programming that encapsulates computations which may either result in a success value or a failure.
 * It's a design pattern that allows you to structure your programs in a way that's easier to reason about, especially when dealing with error handling.
 * Specifically when using teh Operational Result pattern
 * <p>
 * With the exception of the `get` method, all instance methods in the `Outcome` Monad return a new `Outcome` instance.
 * This allows you to chain operations together in a way that's both concise and easy to read.
 * The static methods in the `Outcome` are used to create new instances of the `Outcome` .
 * </p>
 * Here's a simple example of how you might define and use a `Outcome` Monad:
 *
 */

public sealed interface Outcome<SV> permits Success, Failure
{
    /**
     * Checks if the Outcome instance represents a success.
     *
     * @return true if the Outcome instance represents a success, false otherwise.
     */

    boolean isSuccess();

    /**
     * Checks if the Outcome instance represents a failure.
     *
     * @return true if the Outcome instance represents a failure, false otherwise.
     */
    default boolean isFailure() { return !isSuccess(); }

    /**
     * Returns the value contained in this Outcome instance, if it represents a success.
     * If this Outcome instance represents a failure, throws an unchecked exception.
     * The value of a success will always be non-null
     *
     * @return the value contained in this Outcome instance.
     * @throws RuntimeException if this Outcome instance represents a failure.
     */

    SV get();

    /**
     * Returns the optional (potential) value contained in this Outcome instance, if it represents a success.
     * If this Outcome instance represents a failure, returns an empty Optional.
     *
     * @return the value contained in this Outcome instance, if it represents a success; otherwise, an empty Optional.
     */

    default Optional<SV> getPotential()
    {
        return isSuccess() ? Optional.of(get()) : Optional.empty();
    }

    /**
     * Returns the failure represented by this Outcome instance, if it represents a failure.
     * If this Outcome instance represents a success, throws an unchecked exception.
     *
     * @return the failure represented by this Outcome instance.
     * @throws RuntimeException if this Outcome instance represents a success.
     */

    Failure<?> asFailure();

    /**
     * Returns the success represented by this Outcome instance, if it represents a success.
     * If this Outcome instance represents a failure, throws an unchecked exception.
     *
     * @return the success represented by this Outcome instance.
     * @throws RuntimeException if this Outcome instance represents a failure.
     */

    Success<SV> asSuccess();

    /**
     * Transforms the value contained in this Outcome instance using the provided Function, if the Outcome instance represents a success.
     * If this Outcome instance represents a failure, returns the same Outcome instance.
     *
     * @param mapFxn a Function that takes a value of type SV and returns a value of type NV.
     * @param <NV> the type of the value returned by the Function.
     * @return a Outcome instance that contains the transformed value, if this Outcome instance represents a success; otherwise, the same Outcome instance.
     */

    <NV> Outcome<NV> map(Function<SV, NV> mapFxn);

    /**
     * Transforms the value contained in this Outcome instance using the provided Function, if the Outcome instance represents a success.
     * If this Outcome instance represents a failure, returns the same Outcome instance.
     *
     * @param flatMapFxn a Function that takes a value of type SV and returns a Outcome instance that contains a value of type NV.
     * @param <NV> the type of the value contained in the Outcome instance returned by the Function.
     * @return a Outcome instance that contains the transformed value, if this Outcome instance represents a success; otherwise, the same Outcome instance.
     */

    <NV> Outcome<NV> flatMap(Function<SV, Outcome<NV>> flatMapFxn);

    /**
     * Executes the given consumer action with the contained success value if this {@code Outcome} instance is a success.
     * <p>
     * If this instance represents a failure, the consumer action is not executed.
     * </p>
     * @param consumer A {@link Consumer} that will be executed with the contained
     *                 success value of this {@code Outcome} instance if it is a success.
     */

    default void onSuccess(Consumer<SV> consumer)
    {
        if (isSuccess()) {
            consumer.accept(get());
        }
    }

    /**
     * Executes the given consumer with the contained success value if this {@code Outcome} instance is a failure.
     * <p>
     * If this instance represents a success, the consumer action is not executed.
     * </p>
     * @param consumer A {@link Consumer} that will be executed with the
     *                 failure instance of this {@code Outcome} instance if it is a failure.
     */

    default void onFailure(Consumer<Failure> consumer)
    {
        if (isFailure()) {
            consumer.accept(asFailure());
        }
    }

//    Outcome<SV> recover(Function<Failure, SV> recovery);
//    Outcome<SV> recoverWith(Function<Failure, Outcome<SV>> recovery);

    /**
     * Returns the value contained in this Outcome instance, if it represents a success or an alternative.
     * <p>
     * If this Outcome instance represents a failure, returns the provided alternative value.
     *
     * @param supplier the supplier function that returns an outcome to be used as an alternative value.
     * @return the value contained in this Outcome instance, if it represents a success; otherwise, the provided alternative value.
     */

    default Outcome<SV> orElse(Supplier<Outcome<SV>> supplier)
    {
        return isSuccess() ? this : supplier.get();
    }

    /**
     * Returns the value contained in this Outcome instance, if it represents a success or an alternative.
     * <p>
     * If this Outcome instance represents a failure, returns the provided alternative value.
     *
     * @param supplier the supplier function that returns an outcome to be used as an alternative value.
     * @return the value contained in this Outcome instance, if it represents a success; otherwise, the provided
     * alternative value or a Failure if the supplier fails.
     */

    default Outcome<SV> orElse(MurphysSupplier<Outcome<SV>> supplier)
    {
        return isSuccess() ? this : supplier.get();
    }

//    Outcome<SV> orElse(SV alternative);
//    Outcome<SV> orElseThrow();
//    Outcome<SV> orElseThrow(Supplier<RuntimeException> exceptionSupplier);
//    Outcome<SV> orElseThrow(Function<Failure, RuntimeException> exceptionSupplier);
//    Outcome<SV> orElseThrow(Function<Failure, ? extends RuntimeException> exceptionSupplier);
//    Outcome<SV> orElseThrow(Class<? extends RuntimeException> exceptionClass);
//    Outcome<SV> orElseThrow(Class<? extends RuntimeException> exceptionClass, Function<Failure, String> messageSupplier);
//    Outcome<SV> orElseThrow(Class<? extends RuntimeException> exceptionClass, String message);
//    Outcome<SV> orElse

    /**
     * Creates a new Outcome instance that represents the success or failure of the provided operation.
     *
     * @param supplier a MurphysSupplier (Supplier that can throw an exception) that provides the value to be contained in the new Outcome instance.
     * @param <U> the type of the value being supplied.
     *
     * @return a new Outcome instance that represents a success with the provided value.
     */

    public static <U> Outcome<U> attempt(MurphysSupplier<U> supplier) {
        try {
            return new Success(supplier.get());
        }
        catch (Exception e) {
            return new Failure<U>(e);
        }
    }

}
