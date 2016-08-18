package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import javax.inject.Singleton;

public class FrameworkPageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PageMetaFactory.class).to(PageMetaFactoryImpl.class);
        bind(PageNavMenuFactory.class).to(PageNavMenuFactoryImpl.class);
        bind(MultiControllerComponentResolver.class)
                                .annotatedWith(Names.named("controllers"))
                               .toProvider(RoutesMultiControllerComponentResolverProvider.class)
                               .in(Singleton.class);
    }
}
