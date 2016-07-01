package com.commercetools.sunrise.hooks;

public interface SearchFilterHook<T> extends Hook {

    T filterQuery(T query);
}
