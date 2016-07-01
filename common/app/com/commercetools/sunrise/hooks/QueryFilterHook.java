package com.commercetools.sunrise.hooks;

public interface QueryFilterHook<T> extends Hook {

    T filterQuery(T query);
}
