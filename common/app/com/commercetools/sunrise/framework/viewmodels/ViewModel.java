package com.commercetools.sunrise.framework.viewmodels;

import com.commercetools.sunrise.framework.SunriseModel;

import java.util.HashMap;
import java.util.Map;

public abstract class ViewModel extends SunriseModel {

    private Map<String, Object> extendedViewModel;

    public ViewModel() {
    }

    public void put(final String key, final Object value) {
        if (this.extendedViewModel == null) {
            this.extendedViewModel = new HashMap<>();
        }
        this.extendedViewModel.put(key, value);
    }

    public Object get(final String key) {
        if (this.extendedViewModel == null) {
            return null;
        }
        return this.extendedViewModel.get(key);
    }
}
