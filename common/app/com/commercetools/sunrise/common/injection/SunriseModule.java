package com.commercetools.sunrise.common.injection;

import com.commercetools.sunrise.common.pages.RoutesMultiControllerComponentResolverProvider;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import javax.inject.Singleton;

public class SunriseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MultiControllerComponentResolver.class)
                .annotatedWith(Names.named("controllers"))
                .toProvider(RoutesMultiControllerComponentResolverProvider.class)
                .in(Singleton.class);
    }
}
