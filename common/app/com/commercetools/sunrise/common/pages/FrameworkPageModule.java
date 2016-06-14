package com.commercetools.sunrise.common.pages;

import com.google.inject.AbstractModule;

public class FrameworkPageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PageMetaFactory.class).to(PageMetaFactoryImpl.class);
        bind(PageNavMenuFactory.class).to(PageNavMenuFactoryImpl.class);
    }
}
