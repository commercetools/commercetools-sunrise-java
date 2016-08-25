package com.commercetools.sunrise.common.reverserouter;

public final class ReflectionUtils {

    public static Class<?> getClassByName(final String className) throws ClassNotFoundException {
        return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
    }

}
