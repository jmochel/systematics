package org.saltations.systematics.core;

import java.util.function.Supplier;

@FunctionalInterface
public interface MurphysSupplier<T> extends Supplier<T>
{
    T supply() throws Exception;

    default T get()
    {
        try
        {
            return supply();
        }
        catch (Throwable e)
        {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }
}
