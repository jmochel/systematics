package org.saltations.systematics.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * The default basic failure type that is used when no other failure type is specified..
 */


public enum BasicFailureType implements FailureType
{
    GENERIC("generic-failure", ""),
    CATASTROPHIC("catastrophic-failure", "");

    private final String title;
    private final String template;

    BasicFailureType(String title, String template)
    {
        this.title = title;
        this.template = template;
    }

    public String title()
    {
        return title;
    }

    public String template()
    {
        return template;
    }
}
