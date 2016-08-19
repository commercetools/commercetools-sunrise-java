package com.commercetools.sunrise.common.httpauth;

import com.commercetools.sunrise.common.WithSunriseApplication;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.routing.Router;
import play.routing.RoutingDsl;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Results.ok;

public class HttpAuthenticationFilterTest extends WithSunriseApplication {

    private static final Router ROUTER = new RoutingDsl().GET("/").routeTo(() -> ok()).build();
    private static final String AUTHENTICATE_HEADER_CONTENT = "Auth required";

    @Test
    public void allowsAccessWhenDisabled() throws Exception {
        run(appWithAuthDisabled(), "/", request -> {
            final WSResponse response = request.get().toCompletableFuture().join();
            assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
        });
    }

    @Test
    public void unauthorizedWhenEnabled() throws Exception {
        run(appWithAuthEnabled(), "/", request -> {
            final WSResponse response = request.get().toCompletableFuture().join();
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
            assertThat(response.getHeader(Http.HeaderNames.WWW_AUTHENTICATE).equals(AUTHENTICATE_HEADER_CONTENT));
        });
    }

    private Application appWithAuthEnabled() {
        return application(httpAuthentication(true, false));
    }

    private Application appWithAuthDisabled() {
        return application(httpAuthentication(false, false));
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

    private static HttpAuthentication httpAuthentication(final boolean enabled, final boolean authorized) {
        return new HttpAuthentication() {
            @Override
            public boolean isEnabled() {
                return enabled;
            }

            @Override
            public String getWwwAuthenticateHeader() {
                return AUTHENTICATE_HEADER_CONTENT;
            }

            @Override
            public boolean isAuthorized(final String rawAuthorizationHttpHeader) {
                return authorized;
            }
        };
    }
}
