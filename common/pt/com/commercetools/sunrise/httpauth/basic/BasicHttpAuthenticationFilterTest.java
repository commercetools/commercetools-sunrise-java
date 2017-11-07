package com.commercetools.sunrise.httpauth.basic;

import com.commercetools.sunrise.httpauth.HttpAuthentication;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.ws.WS;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.routing.Router;
import play.routing.RoutingDsl;
import play.test.WithServer;

import static org.assertj.core.api.Assertions.assertThat;
import static play.inject.Bindings.bind;
import static play.mvc.Results.ok;

public class BasicHttpAuthenticationFilterTest extends WithServer {
    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";
    private static final String REALM = "My Realm";
    private static final String URI = "/";

    @Override
    protected Application provideApplication() {
        final Router router = new RoutingDsl()
                .GET(URI).routeTo(() -> ok())
                .build();
        return new GuiceApplicationBuilder()
                .configure("play.http.filters", "com.commercetools.sunrise.httpauth.basic.BasicHttpAuthenticationFilters")
                .overrides(
                        bind(HttpAuthentication.class).toInstance(new BasicHttpAuthentication(REALM, USERNAME + ":" + PASSWORD)),
                        bind(play.api.routing.Router.class).toInstance(router.asScala()))
                .build();
    }

    @Test
    public void authorizedWhenEnabledAndCredentialsProvided() throws Exception {
        try (WSClient wsClient = WS.newClient(testServer.port())) {
            final WSResponse response = wsClient
                    .url(URI)
                    .setAuth(USERNAME, PASSWORD, WSAuthScheme.BASIC)
                    .get().toCompletableFuture().get();
            assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
        }
    }

    @Test
    public void unauthorizedWhenEnabledAndWrongCredentialsProvided() throws Exception {
        try (WSClient wsClient = WS.newClient(testServer.port())) {
            final WSResponse response = wsClient
                    .url(URI)
                    .setAuth(USERNAME, "wrong", WSAuthScheme.BASIC)
                    .get().toCompletableFuture().get();
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
            assertThat(response.getHeader(Http.HeaderNames.WWW_AUTHENTICATE)).isNull();
        }
    }

    @Test
    public void unauthorizedWhenEnabledAndNoCredentialsProvided() throws Exception {
        try (WSClient wsClient = WS.newClient(testServer.port())) {
            final WSResponse response = wsClient
                    .url(URI)
                    .get().toCompletableFuture().get();
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
            assertThat(response.getHeader(Http.HeaderNames.WWW_AUTHENTICATE)).contains(REALM);
        }
    }
}
