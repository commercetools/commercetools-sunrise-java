package com.commercetools.sunrise.framework.reverserouters;

import com.commercetools.sunrise.framework.SunriseModel;
import play.mvc.Call;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionException;

public final class ReflectionReverseCaller extends SunriseModel implements ReverseCaller {

    private final Method method;
    private final Object object;

    private ReflectionReverseCaller(final Method method, final Object object) {
        this.method = method;
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public Call call(final Object... args) {
        try {
            return (Call) method.invoke(object, args);
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    public static ReflectionReverseCaller of(final Method method, final Object object) {
        return new ReflectionReverseCaller(method, object);
    }
}
