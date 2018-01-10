package com.commercetools.sunrise.core.viewmodels;

public final class PageData extends ViewModel {

    public PageData putField(final String key, final Object value) {
        put(key, value);
        return this;
    }

    public static PageData of() {
        return new PageData();
    }
}
