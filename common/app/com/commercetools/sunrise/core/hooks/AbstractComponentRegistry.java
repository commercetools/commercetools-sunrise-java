package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.components.Component;
import org.slf4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractComponentRegistry implements ComponentRegistry {

    private final Set<Class<? extends Component>> components = new LinkedHashSet<>();

    @Override
    public void add(final Class<? extends Component> componentClass) {
        components.add(componentClass);
        getLogger().debug("Enabled component {}", componentClass.getCanonicalName());
    }

    @Override
    public void remove(final Class<? extends Component> componentClass) {
        components.remove(componentClass);
        getLogger().debug("Disabled component {}", componentClass.getCanonicalName());
    }

    @Override
    public Set<Class<? extends Component>> components() {
        return components;
    }

    protected abstract Logger getLogger();
}
