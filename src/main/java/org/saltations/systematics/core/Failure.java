package org.saltations.systematics.core;

import java.util.function.Function;

/**
 * Represents a failure outcome.
 * The type of the failure is stored in the type field.
 * The title of the failure is stored in the title field.
 * The detail of the failure is stored in the detail field.
 * The cause of the failure is stored in the cause field.
 *
 * @param <SV> The type of the success value.
 */

public record Failure<SV>(FailureType type, String title, String detail, Exception cause) implements Outcome<SV>
{
    public Failure(Exception cause)
    {
        this(BasicFailureType.GENERIC, "", "", cause);
    }

    @Override
    public boolean isSuccess()
    {
        return false;
    }

    @Override
    public SV get()
    {
        throw new IllegalStateException("No success value is present for this failure. See attached cause.", cause);
    }

    @Override
    public Failure<?> asFailure()
    {
        return this;
    }

    @Override
    public Success<SV> asSuccess()
    {
        throw new IllegalStateException("This Failure cannot be used as a Success");
    }

    @Override
    public <NV> Outcome<NV> map(Function<SV, NV> mapFxn)
    {
        return (Outcome<NV>) this;
    }

    @Override
    public <NV> Outcome<NV> flatMap(Function<SV, Outcome<NV>> flatMapFxn)
    {
        return (Outcome<NV>) this;
    }


}
