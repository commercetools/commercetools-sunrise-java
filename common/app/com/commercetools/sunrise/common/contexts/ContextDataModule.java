package com.commercetools.sunrise.common.contexts;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.RequestHookContextImpl;
import com.google.inject.AbstractModule;
import play.mvc.Http;

import javax.inject.Singleton;

/**
 * Module to enable request scoped dependency injection of the HTTP context of play.
 */
public class ContextDataModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Http.Context.class).toProvider(HttpContextProvider.class).in(RequestScoped.class);
        bind(RequestContext.class).toProvider(RequestContextProvider.class).in(RequestScoped.class);
        bind(RequestHookContext.class).to(RequestHookContextImpl.class).in(RequestScoped.class);
    }
}
