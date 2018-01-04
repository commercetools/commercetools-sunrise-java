package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.components.Component;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(HookContextImpl.class)
public interface ComponentRegistry {

    List<Class<? extends Component>> components();

    void add(Class<? extends Component> component);

    default void addAll(Iterable<Class<? extends Component>> components) {
        components.forEach(this::add);
    }
}
