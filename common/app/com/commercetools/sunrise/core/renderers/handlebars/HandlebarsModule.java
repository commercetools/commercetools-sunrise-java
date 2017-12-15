package com.commercetools.sunrise.core.renderers.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Module that allows to inject the Template Engine related classes.
 */
public final class HandlebarsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Handlebars.class)
                .toProvider(HandlebarsProvider.class)
                .in(Singleton.class);
    }
}
