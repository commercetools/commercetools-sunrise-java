package com.commercetools.sunrise.common.models;

import java.util.HashMap;
import java.util.Map;

public abstract class ViewModel extends SunriseModel {

    private final Map<String, Object> extendedViewModel = new HashMap<>();

    public ViewModel() {
    }

    public void put(final String key, final Object value) {
        extendedViewModel.put(key, value);
    }

    public Object get(final String key) {
        return extendedViewModel.get(key);
    }
}
