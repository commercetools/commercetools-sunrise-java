package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.components.Component;
import org.slf4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractComponentRegistry implements ComponentRegistry {

    private final Set<Class<? extends Component>> components = new LinkedHashSet<>();

    @Override
    public void add(final Class<? extends Component> componentClass) {
        getLogger().debug("Registered component {}", componentClass.getCanonicalName());
        components.add(componentClass);
    }

    @Override
    public Set<Class<? extends Component>> components() {
        return components;
    }

    protected abstract Logger getLogger();
}
