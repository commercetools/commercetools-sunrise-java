package com.commercetools.sunrise.framework.injection;

import com.commercetools.sunrise.framework.injection.RequestScope;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.test.WithApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.invokeWithContext;

public class RequestScopedTest extends WithApplication {

    private static final String MARKER = "marker";

    @Test
    public void keepsAliveInTheSameRequest() throws Exception {
        invokeWithContext(fakeRequest().header(MARKER, "1"), () -> {
            final RequestScopedClass instance1 = app.injector().instanceOf(RequestScopedClass.class);
            final RequestScopedClass instance2 = app.injector().instanceOf(RequestScopedClass.class);
            assertThat(instance1)
                    .as("Request scoped instance is kept alive in the same request")
                    .isSameAs(instance2);
            return null;
        });
    }

    @Test
    public void createsANewInstanceBetweenRequests() throws Exception {
        final RequestScopedClass instance1 = invokeWithContext(fakeRequest().header(MARKER, "1"), () ->
                app.injector().instanceOf(RequestScopedClass.class));
        final RequestScopedClass instance2 = invokeWithContext(fakeRequest().header(MARKER, "2"), () ->
                app.injector().instanceOf(RequestScopedClass.class));
        assertThat(instance1.getContext().request().getHeader(MARKER)).isEqualTo("1");
        assertThat(instance2.getContext().request().getHeader(MARKER)).isEqualTo("2");
        assertThat(instance1)
                .as("Request scoped instance is new with every request")
                .isNotSameAs(instance2);
    }

    @Test
    public void singletonIsNotAffected() throws Exception {
        final NotRequestScopedClass instance1 = invokeWithContext(fakeRequest(), () ->
                app.injector().instanceOf(NotRequestScopedClass.class));
        final NotRequestScopedClass instance2 = invokeWithContext(fakeRequest(), () ->
                app.injector().instanceOf(NotRequestScopedClass.class));
        assertThat(instance1)
                .as("Singletons are reused among requests")
                .isSameAs(instance2);
    }

    @Override
    protected Application provideApplication() {
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                final RequestScope requestScope = new RequestScope();
                bindScope(RequestScoped.class, requestScope);
            }

            @Provides
            @RequestScoped
            public Http.Context httpContext() {
                return Http.Context.current();
            }
        };
        return new GuiceApplicationBuilder()
                .overrides(module)
                .build();
    }

    @RequestScoped
    private static class RequestScopedClass {
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
    private static class NotRequestScopedClass {
        @Inject
        private Configuration configuration;

        public Configuration getConfiguration() {
            return configuration;
        }
    }
}
