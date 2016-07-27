package com.commercetools.sunrise.common.basicauth;

import com.commercetools.sunrise.common.WithSunriseApplication;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Providers;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.routing.Router;
import play.routing.RoutingDsl;

import javax.annotation.Nullable;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Results.ok;

public class BasicAuthFilterTest extends WithSunriseApplication {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final Router ROUTER = new RoutingDsl().GET("/").routeTo(() -> ok()).build();
    private static final BasicAuth BASIC_AUTH_ENABLED = BasicAuth.of("My Realm", USERNAME + ":" + PASSWORD);
    private static final BasicAuth BASIC_AUTH_DISABLED = null;

    @Test
    public void allowsAccessWhenDisabled() throws Exception {
        run(appWithBasicAuthDisabled(), "/", request -> {
            final WSResponse response = request.get().toCompletableFuture().join();
            assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
        });
    }

    @Test
    public void unauthorizedWhenEnabled() throws Exception {
        run(appWithBasicAuthEnabled(), "/", request -> {
            final WSResponse response = request.get().toCompletableFuture().join();
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
        });
    }

    @Test
    public void authorizedWhenEnabledAndCredentialsProvided() throws Exception {
        run(appWithBasicAuthEnabled(), "/", request -> {
            final WSResponse response = request
                    .setAuth(USERNAME, PASSWORD, WSAuthScheme.BASIC)
                    .get().toCompletableFuture().join();
            assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
        });
    }

    @Test
    public void unauthorizedWhenEnabledAndWrongCredentialsProvided() throws Exception {
        run(appWithBasicAuthEnabled(), "/", request -> {
            final WSResponse response = request
                    .setAuth(USERNAME, "wrong", WSAuthScheme.BASIC)
                    .get().toCompletableFuture().join();
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
        });
    }

    private Application appWithBasicAuthEnabled() {
        return application(BASIC_AUTH_ENABLED);
    }

    private Application appWithBasicAuthDisabled() {
        return application(BASIC_AUTH_DISABLED);
    }

    private Application application(final @Nullable BasicAuth basicAuth) {
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(BasicAuth.class).toProvider(Providers.of(basicAuth));
                bind(play.api.routing.Router.class).toInstance(ROUTER.asScala());
            }
        };
        return appBuilder(module)
                .loadConfig(configurationWithBasicAuthFilterEnabled())
                .build();
    }

    private Configuration configurationWithBasicAuthFilterEnabled() {
        final Map<String, Object> configMap = singletonMap("play.http.filters", "com.commercetools.sunrise.play.SunriseHttpFilters");
        return new Configuration(configMap).withFallback(testConfiguration());
    }
}
