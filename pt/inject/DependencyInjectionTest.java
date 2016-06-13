package inject;

import com.commercetools.sunrise.WithSunriseApplication;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.inject.Injector;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyInjectionTest extends WithSunriseApplication {
    @Test
    public void allowsAccessWhenDisabled() throws Exception {
        final Application application = appBuilder()
                .build();
        run(application, "/de/home", request -> {
            final Injector injector = application.injector();
            setNewContextWithMarkerHeaderValueOf("1");
            final SomethingRequestScoped requestScoped1 = injector.instanceOf(SomethingRequestScoped.class);
            assertThat(requestScoped1).isNotNull();
            final NotRequestScoped notRequestScoped = injector.instanceOf(NotRequestScoped.class);

            setNewContextWithMarkerHeaderValueOf("2");
            final SomethingRequestScoped requestScoped2 = injector.instanceOf(SomethingRequestScoped.class);
            assertThat(requestScoped1.getContext().request().getHeader("marker")).isEqualTo("1");
            assertThat(requestScoped2.getContext().request().getHeader("marker")).isEqualTo("2");
            assertThat(requestScoped1)
                    .as("request scoped stuff is new with every request")
                    .isNotSameAs(requestScoped2);
            assertThat(injector.instanceOf(NotRequestScoped.class))
                    .as("singletons are reused among requests")
                    .isSameAs(notRequestScoped);
        });
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
