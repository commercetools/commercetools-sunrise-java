package com.commercetools.sunrise.common.basicauth;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class BasicAuthModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BasicAuth.class).toProvider(BasicAuthProvider.class).in(Singleton.class);
    }
}
