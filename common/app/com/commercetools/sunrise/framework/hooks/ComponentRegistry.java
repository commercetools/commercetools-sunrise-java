package com.commercetools.sunrise.framework.hooks;

import com.commercetools.sunrise.framework.components.SunriseComponent;
import com.google.inject.ImplementedBy;

@ImplementedBy(HookContextImpl.class)
public interface ComponentRegistry {

    void add(SunriseComponent component);

    default void addAll(Iterable<? extends SunriseComponent> components) {
        components.forEach(this::add);
    }
}
