package com.commercetools.sunrise.common.httpauth.basic;

import com.commercetools.sunrise.common.WithSunriseApplication;
import com.commercetools.sunrise.common.httpauth.HttpAuthentication;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.routing.Router;
import play.routing.RoutingDsl;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Results.ok;

public class BasicHttpAuthenticationFilterTest extends WithSunriseApplication {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REALM = "My Realm";
    private static final Router ROUTER = new RoutingDsl().GET("/").routeTo(() -> ok()).build();

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
            assertThat(response.getHeader(Http.HeaderNames.WWW_AUTHENTICATE)).isNullOrEmpty();
        });
    }

    @Test
    public void unauthorizedWhenEnabledAndNoCredentialsProvided() throws Exception {
        run(appWithBasicAuthEnabled(), "/", request -> {
            final WSResponse response = request.get().toCompletableFuture().join();
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
            assertThat(response.getHeader(Http.HeaderNames.WWW_AUTHENTICATE)).contains(REALM);
        });
    }

    private Application appWithBasicAuthEnabled() {
        return application(new BasicHttpAuthentication(REALM, USERNAME + ":" + PASSWORD));
    }

    private Application application(final HttpAuthentication httpAuthentication) {
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(HttpAuthentication.class).toInstance(httpAuthentication);
                bind(play.api.routing.Router.class).toInstance(ROUTER.asScala());
            }
        };
        return appBuilder(module)
                .loadConfig(configurationWithBasicAuthFilterEnabled())
                .build();
    }

    private Configuration configurationWithBasicAuthFilterEnabled() {
        final Map<String, Object> configMap = singletonMap("play.http.filters", "com.commercetools.sunrise.play.http.SunriseHttpFilters");
        return new Configuration(configMap).withFallback(testConfiguration());
    }
}
