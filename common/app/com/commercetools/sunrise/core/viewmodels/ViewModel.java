package com.commercetools.sunrise.core.viewmodels;

import com.commercetools.sunrise.core.SunriseModel;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class ViewModel extends SunriseModel {

    private Map<String, Object> extendedViewModel;

    public ViewModel() {
    }

    @Nullable
    protected Map<String, Object> map() {
        return extendedViewModel;
    }

    public Object get(final String key) {
        if (this.extendedViewModel == null) {
            return null;
        }
        return this.extendedViewModel.get(key);
    }

    public void put(final String key, final Object value) {
        if (this.extendedViewModel == null) {
            this.extendedViewModel = new HashMap<>();
        }
        this.extendedViewModel.put(key, value);
    }
}
