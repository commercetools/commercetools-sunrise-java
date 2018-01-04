package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.components.Component;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractComponentRegistry implements ComponentRegistry {

    private final List<Class<? extends Component>> components = new LinkedList<>();

    @Override
    public void add(final Class<? extends Component> componentClass) {
        getLogger().debug("Registered component {}", componentClass.getCanonicalName());
        components.add(componentClass);
    }

    @Override
    public List<Class<? extends Component>> components() {
        return components;
    }

    protected abstract Logger getLogger();
}
