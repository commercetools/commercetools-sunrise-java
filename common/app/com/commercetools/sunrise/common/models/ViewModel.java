package com.commercetools.sunrise.common.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ViewModel extends SunriseModel {

    private final Map<String, Object> dynamic = new HashMap<>();

    public ViewModel() {
    }

    public final void addDynamic(final String attributeName, final Object object) {
        dynamic.put(attributeName, object);
    }

    public Map<String, Object> getDynamic() {
        return Collections.unmodifiableMap(dynamic);
    }
}
