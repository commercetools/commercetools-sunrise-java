package com.commercetools.sunrise.common.inject.ctpmodels;

import com.google.inject.AbstractModule;
import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.models.ProductDataConfig;

import javax.inject.Singleton;

/**
 * Configuration for the Guice {@link com.google.inject.Injector} which shall be used in production.
 */
public class CtpModelsProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProjectContext.class).toProvider(ProjectContextProvider.class).in(Singleton.class);
        bind(ProductDataConfig.class).toProvider(ProductDataConfigProvider.class).in(Singleton.class);
    }
}
