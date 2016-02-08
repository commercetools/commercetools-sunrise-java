package basicauth;

import common.controllers.TestableReverseRouter;
import controllers.WithSunriseApplication;
import org.junit.Test;
import play.Application;
import play.Configuration;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import reverserouter.ReverseRouterTestModule;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class BasicAuthRequestHandlerTest extends WithSunriseApplication {
    private static final Configuration CONFIG = configurationWithHandleEnabled();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Test
    public void allowsAccessWhenDisabled() throws Exception {
        run(appWithoutBasicAuth(), "/", request -> {
            final WSResponse response = request.get().get(ALLOWED_TIMEOUT);
            assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
        });
    }

    @Test
    public void unauthorizedWhenEnabled() throws Exception {
        run(appWithBasicAuth(), "/", request -> {
            final WSResponse response = request.get().get(ALLOWED_TIMEOUT);
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
        });
    }

    @Test
    public void authorizedWhenEnabledAndCredentialsProvided() throws Exception {
        run(appWithBasicAuth(), "/", request -> {
            final WSRequest authorizedRequest = request.setAuth(USERNAME, PASSWORD, WSAuthScheme.BASIC);
            final WSResponse response = authorizedRequest.get().get(ALLOWED_TIMEOUT);
            assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
        });
    }

    @Test
    public void unauthorizedWhenEnabledAndWrongCredentialsProvided() throws Exception {
        run(appWithBasicAuth(), "/", request -> {
            final WSRequest unauthorizedRequest = request.setAuth(USERNAME, "wrong", WSAuthScheme.BASIC);
            final WSResponse response = unauthorizedRequest.get().get(ALLOWED_TIMEOUT);
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
        });
    }

    private static Application appWithoutBasicAuth() {
        return appWithBasicAuth(null);
    }

    private static Application appWithBasicAuth() {
        return appWithBasicAuth(BasicAuth.of("My Realm", USERNAME + ":" + PASSWORD));
    }

    private static Application appWithBasicAuth(final BasicAuth basicAuth) {
        final TestableReverseRouter reverseRouter = new TestableReverseRouter();
        reverseRouter.setHomeUrl("/en/home");
        return appBuilder(new BasicAuthTestModule(basicAuth), new ReverseRouterTestModule(reverseRouter))
                .loadConfig(CONFIG)
                .build();
    }

    private static Configuration configurationWithHandleEnabled() {
        final Map<String, Object> configMap = singletonMap("play.http.requestHandler", "basicauth.BasicAuthRequestHandler");
        return new Configuration(configMap).withFallback(testConfiguration());
    }
}
