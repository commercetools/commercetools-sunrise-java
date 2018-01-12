package com.commercetools.sunrise.core.viewmodels;

import io.sphere.sdk.models.Base;

import java.util.HashMap;
import java.util.Map;

public final class PageData extends Base {

    private Map<String, Object> extendedViewModel;

    private PageData() {
    }

    public Object get(final String key) {
        if (this.extendedViewModel == null) {
            return null;
        }
        return this.extendedViewModel.get(key);
    }

    public PageData put(final String key, final Object value) {
        if (this.extendedViewModel == null) {
            this.extendedViewModel = new HashMap<>();
        }
        this.extendedViewModel.put(key, value);
        return this;
    }

    public PageData putAll(final PageData pageData) {
        if (this.extendedViewModel == null) {
            this.extendedViewModel = new HashMap<>();
        }
        this.extendedViewModel.putAll(pageData.extendedViewModel);
        return this;
    }

    public static PageData of() {
        return new PageData();
    }
}
