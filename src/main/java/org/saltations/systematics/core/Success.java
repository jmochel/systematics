package org.saltations.systematics.core;

import java.util.function.Function;

import lombok.NonNull;

/**
 * Represents a successful, or partially successful, outcome.
 * The value of the success is stored in the value field.
 *
 * @param <SV> The type of the success value.
 */

public record Success<SV>(@NonNull SV value) implements Outcome<SV>
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
    public <NV> Outcome<NV> map(Function<SV, NV> mapFxn)
    {
        return new Success<>(mapFxn.apply(value));
    }

    @Override
    public <NV> Outcome<NV> flatMap(Function<SV, Outcome<NV>> flatMapFxn)
    {
        return flatMapFxn.apply(value);
    }

}
