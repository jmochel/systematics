package org.saltations.systematics.core;

import java.util.function.Function;

import lombok.NonNull;

/**
 * Represents a successful, or partially successful, outcome.
 * The value of the success is stored in the value field.
 *
 * @param <SV> The type of the success value.
 */

public record Success<SV>(@NonNull SV value) implements Try<SV>
{
    public static final Success<Boolean> SUCCESS = new Success<>(true);

    @Override
    public boolean isSuccess()
    {
        return true;
    }

    @Override
    public SV get()
    {
        return value;
    }

    @Override
    public Failure<?> asFailure()
    {
        throw new IllegalStateException("This Success cannot be used as a failure.");
    }

    @Override
    public Success<SV> asSuccess()
    {
        return this;
    }

    @Override
    public <NV> Try<NV> map(Function<SV, NV> mapFxn)
    {
        return new Success<>(mapFxn.apply(value));
    }

    @Override
    public <NV> Try<NV> flatMap(Function<SV, Try<NV>> flatMapFxn)
    {
        return flatMapFxn.apply(value);
    }

}
