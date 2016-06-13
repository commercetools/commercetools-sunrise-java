package com.commercetools.sunrise.common.models;

import com.google.inject.AbstractModule;
import com.commercetools.sunrise.common.pages.PageMetaFactory;

public class FrameworkWireModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PageMetaFactory.class).to(PageMetaFactoryImpl.class);
        bind(NavMenuDataFactory.class).to(NavMenuDataFactoryImpl.class);
    }
}
