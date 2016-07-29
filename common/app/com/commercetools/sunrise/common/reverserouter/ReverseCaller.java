package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

import java.lang.reflect.Method;
import java.util.concurrent.CompletionException;

public class ReverseCaller {

    private final Method method;
    private final Object object;

    public ReverseCaller(final Method method, final Object object) {
        this.method = method;
        this.object = object;
    }

    public Call call(final Object... args) {
        try {
            return (Call) method.invoke(object, args);
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }
}
