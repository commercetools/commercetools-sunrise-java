package common;

import com.google.inject.AbstractModule;
import common.contexts.UserContext;
import common.inject.*;
import play.mvc.Http;

/**
 * Module to enable request scoped dependency injection of the HTTP context of play.
 */
public class HttpModule extends AbstractModule {

    @Override
    protected void configure() {
        final HttpContextScope httpContextScope = new HttpContextScope();
        bindScope(HttpContextScoped.class, httpContextScope);
        bind(Http.Context.class).toProvider(HttpContextProvider.class).in(httpContextScope);
        bind(UserContext.class).toProvider(UserContextProvider.class).in(httpContextScope);
    }
}
