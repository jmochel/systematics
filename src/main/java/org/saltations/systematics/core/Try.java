package org.saltations.systematics.core;

import java.util.Optional;
import java.util.function.Function;

/**
 * The `Try` Monad is a concept in functional programming that encapsulates computations which may either result in a success value or a failure.
 * It's a design pattern that allows you to structure your programs in a way that's easier to reason about, especially when dealing with error handling.
 * Specifically when using teh Operational Result pattern
 * <p>
 * With the exception of the `get` method, all instance methods in the `Try` Monad return a new `Try` instance.
 * This allows you to chain operations together in a way that's both concise and easy to read.
 * The static methods in the `Try` are used to create new instances of the `Try` .
 * </p>
 * Here's a simple example of how you might define and use a `Try` Monad:
 *
 */

public sealed interface Try<SV> permits Success, Failure
{
    /**
     * Checks if the Try instance represents a success.
     *
     * @return true if the Try instance represents a success, false otherwise.
     */

    boolean isSuccess();

    /**
     * Checks if the Try instance represents a failure.
     *
     * @return true if the Try instance represents a failure, false otherwise.
     */
    default boolean isFailure() { return !isSuccess(); }

    /**
     * Returns the value contained in this Try instance, if it represents a success.
     * If this Try instance represents a failure, throws an unchecked exception.
     * The value of a success will always be non-null
     *
     * @return the value contained in this Try instance.
     * @throws RuntimeException if this Try instance represents a failure.
     */

    SV get();

    /**
     * Returns the optional (potential) value contained in this Try instance, if it represents a success.
     * If this Try instance represents a failure, returns an empty Optional.
     *
     * @return the value contained in this Try instance, if it represents a success; otherwise, an empty Optional.
     */

    default Optional<SV> getPotential()
    {
        return isSuccess() ? Optional.of(get()) : Optional.empty();
    }

    /**
     * Returns the failure represented by this Try instance, if it represents a failure.
     * If this Try instance represents a success, throws an unchecked exception.
     *
     * @return the failure represented by this Try instance.
     * @throws RuntimeException if this Try instance represents a success.
     */

    Failure<?> asFailure();

    /**
     * Returns the success represented by this Try instance, if it represents a success.
     * If this Try instance represents a failure, throws an unchecked exception.
     *
     * @return the success represented by this Try instance.
     * @throws RuntimeException if this Try instance represents a failure.
     */

    Success<SV> asSuccess();

    /**
     * Transforms the value contained in this Try instance using the provided Function, if the Try instance represents a success.
     * If this Try instance represents a failure, returns the same Try instance.
     *
     * @param mapFxn a Function that takes a value of type SV and returns a value of type NV.
     * @param <NV> the type of the value returned by the Function.
     * @return a Try instance that contains the transformed value, if this Try instance represents a success; otherwise, the same Try instance.
     */

    <NV> Try<NV> map(Function<SV, NV> mapFxn);

    /**
     * Transforms the value contained in this Try instance using the provided Function, if the Try instance represents a success.
     * If this Try instance represents a failure, returns the same Try instance.
     *
     * @param flatMapFxn a Function that takes a value of type SV and returns a Try instance that contains a value of type NV.
     * @param <NV> the type of the value contained in the Try instance returned by the Function.
     * @return a Try instance that contains the transformed value, if this Try instance represents a success; otherwise, the same Try instance.
     */

    <NV> Try<NV> flatMap(Function<SV, Try<NV>> flatMapFxn);



//    void ifSuccess(Consumer<SV> consumer);
//    void ifFailure(Consumer<Failure> consumer);
//    <NV> Try<NV> map(Function<SV, NV> mapper);
//    <NV> Try<NV> flatMap(Function<SV, Try<NV>> mapper);
//    Try<SV> recover(Function<Failure, SV> recovery);
//    Try<SV> recoverWith(Function<Failure, Try<SV>> recovery);
//    Try<SV> orElse(Supplier<Try<SV>> alternative);
//    Try<SV> orElse(SV alternative);
//    Try<SV> orElseThrow();
//    Try<SV> orElseThrow(Supplier<RuntimeException> exceptionSupplier);
//    Try<SV> orElseThrow(Function<Failure, RuntimeException> exceptionSupplier);
//    Try<SV> orElseThrow(Function<Failure, ? extends RuntimeException> exceptionSupplier);
//    Try<SV> orElseThrow(Class<? extends RuntimeException> exceptionClass);
//    Try<SV> orElseThrow(Class<? extends RuntimeException> exceptionClass, Function<Failure, String> messageSupplier);
//    Try<SV> orElseThrow(Class<? extends RuntimeException> exceptionClass, String message);
//    Try<SV> orElse

    /**
     * Creates a new Try instance that represents the success or failure of the provided operation.
     *
     * @param supplier a MurphysSupplier (Supplier that can throw an exception) that provides the value to be contained in the new Try instance.
     * @param <U> the type of the value being supplied.
     *
     * @return a new Try instance that represents a success with the provided value.
     */

    public static <U> Try<U> attempt(MurphysSupplier<U> supplier) {
        try {
            return new Success(supplier.get());
        }
        catch (Exception e) {
            return new Failure<U>(e);
        }
    }

}
