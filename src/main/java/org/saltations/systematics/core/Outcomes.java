package org.saltations.systematics.core;

import java.text.MessageFormat;

/**
 * Factory for ceating the outcomes of an operation.
 */

public class Outcomes
{
    /**
     * Create a default success outcome.
     */

    public static Success<Boolean> success()
    {
        return Success.SUCCESS;
    }

    /**
     * Create a success outcome with the given value.
     *
     * @param value The value of the success.
     */

    public static <SV> Success<SV> success(SV value)
    {
        return new Success<>(value);
    }

    /**
     * Create a generic failure outcome with no additional info.
     */

    public static Failure<?> genericFailure()
    {
        return new Failure<>(BasicFailureType.GENERIC, "Generic failure", "", null);
    }

    /**
     * Create a failure outcome with the given title, template, and detail.
     */

    public static <SV> Failure<SV> genericFailure(String title, String template, Object...args)
    {
        return new Failure<>(BasicFailureType.GENERIC, title, MessageFormat.format(template, args), null);
    }

    /**
     * Create a failure outcome with just given title.
     */

    public static <SV> Failure<SV> genericFailure(String title)
    {
        return new Failure<>(BasicFailureType.GENERIC, title, "", null);
    }

    /**
     * Create a failure outcome with the given cause.
     *
     * @param cause The cause of the failure.
     */

    public static <SV> Failure<SV> causedFailure(Exception cause)
    {
        return new Failure<>(cause);
    }

    /**
     * Create a failure outcome with the title
     *
     * @param title The title of the failure.
     */

    public static <SV> Failure<SV> causedFailure(Exception cause, String title)
    {
        return new Failure<>(BasicFailureType.GENERIC, title, "", cause);
    }

    /**
     * Create a caused failure outcome with the given cause, title and details.
     *
     * @param cause The cause of the failure.
     * @param title The title of the failure.
     * @param template The template of the failure.
     * @param args The arguments of the template .
     */

    public static <SV> Failure<SV> causedFailure(Exception cause, String title, String template, Object...args)
    {
        return new Failure<>(BasicFailureType.GENERIC, title, MessageFormat.format(template, args), cause);
    }

    /**
     * Create a typed failure outcome with the given type and title.
     *
     * @param type The type of the failure.
     * @param args The arguments for the FailureType's template.
     */

    public static <SV> Failure<SV> typedFailure(FailureType type, Object...args)
    {
        return new Failure<>(type, type.title(), MessageFormat.format(type.template(), args), null);
    }

}
