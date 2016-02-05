package basicauth;

import common.controllers.TestableReverseRouter;
import controllers.WithSunriseApplication;
import org.junit.Test;
import play.Application;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import reverserouter.ReverseRouterTestModule;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicAuthRequestHandlerTest extends WithSunriseApplication {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

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
            final WSRequest authenticatedRequest = request.setAuth(USERNAME, PASSWORD, WSAuthScheme.BASIC);
            final WSResponse response = authenticatedRequest.get().get(ALLOWED_TIMEOUT);
            assertThat(response.getStatus()).isEqualTo(Http.Status.OK);
        });
    }

    @Test
    public void unauthorizedWhenEnabledAndWrongCredentialsProvided() throws Exception {
        run(appWithBasicAuth(), "/", request -> {
            final WSRequest authenticatedRequest = request.setAuth(USERNAME, "wrong", WSAuthScheme.BASIC);
            final WSResponse response = authenticatedRequest.get().get(ALLOWED_TIMEOUT);
            assertThat(response.getStatus()).isEqualTo(Http.Status.UNAUTHORIZED);
        });
    }

    private Application appWithoutBasicAuth() {
        return appWithBasicAuth(null);
    }

    private Application appWithBasicAuth() {
        return appWithBasicAuth(BasicAuth.of("My Realm", USERNAME + ":" + PASSWORD));
    }

    private Application appWithBasicAuth(final BasicAuth basicAuth) {
        final TestableReverseRouter reverseRouter = new TestableReverseRouter();
        reverseRouter.setHomeUrl("/en/home");
        return app(new BasicAuthTestModule(basicAuth), new ReverseRouterTestModule(reverseRouter));
    }
}
