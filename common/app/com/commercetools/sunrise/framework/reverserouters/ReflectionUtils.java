package com.commercetools.sunrise.framework.reverserouters;

final class ReflectionUtils {

    static Class<?> getClassByName(final String className) throws ClassNotFoundException {
        return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
    }
}
