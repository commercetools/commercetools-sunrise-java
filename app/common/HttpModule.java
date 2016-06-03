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
        final RequestScope requestScope = new RequestScope();
        bindScope(RequestScoped.class, requestScope);
        bind(Http.Context.class).toProvider(HttpContextProvider.class).in(requestScope);
        bind(UserContext.class).toProvider(UserContextProvider.class).in(requestScope);
    }
}
