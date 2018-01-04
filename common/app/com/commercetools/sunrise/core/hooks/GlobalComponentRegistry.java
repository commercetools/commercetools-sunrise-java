package com.commercetools.sunrise.core.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GlobalComponentRegistry extends AbstractComponentRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalComponentRegistry.class);

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
