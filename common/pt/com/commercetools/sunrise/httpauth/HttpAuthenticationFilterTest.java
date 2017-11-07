package com.commercetools.sunrise.httpauth;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.routing.Router;
import play.routing.RoutingDsl;
import play.test.Helpers;
import play.test.TestServer;

import static org.assertj.core.api.Assertions.assertThat;
import static play.inject.Bindings.bind;
import static play.mvc.Results.ok;
import static play.test.Helpers.running;

public class HttpAuthenticationFilterTest {

    private static final String AUTHENTICATE_HEADER_CONTENT = "Auth required";
    private static final String URI = "/";

    private TestServer testServer(final boolean isAuthEnabled) {
        final Router router = new RoutingDsl()
                .GET(URI).routeTo(() -> ok())
                .build();
        final Application app = new GuiceApplicationBuilder()
                .configure("play.http.filters", "com.commercetools.sunrise.httpauth.basic.BasicHttpAuthenticationFilters")
                .overrides(
                        bind(HttpAuthentication.class).toInstance(httpAuthentication(isAuthEnabled)),
                        bind(play.api.routing.Router.class).toInstance(router.asScala()))
                .build();
        return Helpers.testServer(app);
    }

    @Test
    public void allowsAccessWhenDisabled() throws Exception {
        final TestServer testServer = testServer(false);
        running(testServer, () -> {
            try (WSClient wsClient = WS.newClient(testServer.port())) {
                final WSResponse response = wsClient
                        .url(URI)
                        .get().toCompletableFuture().get();
                assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void unauthorizedWhenEnabled() throws Exception {
        final TestServer testServer = testServer(true);
        running(testServer, () -> {
            try (WSClient wsClient = WS.newClient(testServer.port())) {
                final WSResponse response = wsClient
                        .url(URI)
                        .get().toCompletableFuture().get();
                assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
                assertThat(response.getHeader(Http.HeaderNames.WWW_AUTHENTICATE).equals(AUTHENTICATE_HEADER_CONTENT));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static HttpAuthentication httpAuthentication(final boolean enabled) {
        return new HttpAuthentication() {
            @Override
            public boolean isEnabled() {
                return enabled;
            }

            @Override
            public String getWwwAuthenticateHeaderValue() {
                return AUTHENTICATE_HEADER_CONTENT;
            }

            @Override
            public boolean isAuthorized(final String rawAuthorizationHttpHeader) {
                return false;
            }
        };
    }
}
