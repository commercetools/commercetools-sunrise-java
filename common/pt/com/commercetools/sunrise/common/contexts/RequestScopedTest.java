package com.commercetools.sunrise.common.contexts;

import com.commercetools.sunrise.common.WithSunriseApplication;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.inject.Injector;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestScopedTest extends WithSunriseApplication {

    @Test
    public void allowsAccessWhenDisabled() throws Exception {
        final Application application = application();
        final Injector injector = application.injector();
        run(application, "/de/home", request -> {
            setNewContextWithMarkerHeaderValueOf("1");
            final SomethingRequestScoped requestScoped1 = injector.instanceOf(SomethingRequestScoped.class);
            final NotRequestScoped notRequestScoped1 = injector.instanceOf(NotRequestScoped.class);

            setNewContextWithMarkerHeaderValueOf("2");
            final SomethingRequestScoped requestScoped2 = injector.instanceOf(SomethingRequestScoped.class);
            final NotRequestScoped notRequestScoped2 = injector.instanceOf(NotRequestScoped.class);

            assertThat(requestScoped1.getContext().request().getHeader("marker")).isEqualTo("1");
            assertThat(requestScoped2.getContext().request().getHeader("marker")).isEqualTo("2");

            assertThat(requestScoped1)
                    .as("request scoped stuff is new with every request")
                    .isNotSameAs(requestScoped2);

            assertThat(notRequestScoped2)
                    .as("singletons are reused among requests")
                    .isSameAs(notRequestScoped1);
        });
    }

    private Application application() {
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                final RequestScope requestScope = new RequestScope();
                bindScope(RequestScoped.class, requestScope);
                bind(Http.Context.class).toProvider(HttpContextProvider.class).in(requestScope);

            }
        };
        return appBuilder(module).build();
    }

    private void setNewContextWithMarkerHeaderValueOf(final String marker) {
        setContext(new Http.RequestBuilder().header("marker", marker).build());
    }

    @RequestScoped
    private static class SomethingRequestScoped {
        @Inject
        private Configuration configuration;
        @Inject
        private Http.Context context;

        public Configuration getConfiguration() {
            return configuration;
        }

        public Http.Context getContext() {
            return context;
        }
    }

    @Singleton
    private static class NotRequestScoped {
        @Inject
        private Configuration configuration;

        public Configuration getConfiguration() {
            return configuration;
        }
    }
}
