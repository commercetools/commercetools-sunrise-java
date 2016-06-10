package com.commercetools.sunrise.common.inject;

import com.google.inject.AbstractModule;
import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.inject.*;
import play.mvc.Http;

/**
 * Module to enable request scoped dependency injection of the HTTP context of play.
 */
public class SunriseFrameworkProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        final RequestScope requestScope = new RequestScope();
        bindScope(RequestScoped.class, requestScope);
        bind(Http.Context.class).toProvider(HttpContextProvider.class).in(requestScope);
        bind(UserContext.class).toProvider(UserContextProvider.class).in(requestScope);
        bind(RequestContext.class).toProvider(RequestContextProvider.class).in(requestScope);
    }
}
