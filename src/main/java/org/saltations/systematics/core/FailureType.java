package org.saltations.systematics.core;

import java.util.regex.Pattern;

/**
 * Represents a type of failure.
 * A failure type has a title and a template for the failure details message.
 *
 * TODO - Does the template need to be here ??
 */

public interface FailureType
{
    String title();
    String template();

    /**
     * Count the number of template parameters needed by the template.
     */

    default long templateParameterCount()
    {
        return Pattern.compile("\\{\\}")
                      .matcher(template())
                      .results()
                      .count();
    }

}
