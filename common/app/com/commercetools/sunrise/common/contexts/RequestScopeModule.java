package com.commercetools.sunrise.common.contexts;

import com.google.inject.AbstractModule;

public class RequestScopeModule extends AbstractModule {

    @Override
    protected void configure() {
        bindScope(RequestScoped.class, new RequestScope());
    }
}
