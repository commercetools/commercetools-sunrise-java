package basicauth;

import controllers.ApplicationController;
import controllers.WithSunriseApplication;
import org.junit.Ignore;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicAuthRequestHandlerTest extends WithSunriseApplication {

    @Test
    public void allowsAccessWhenBasicAuthDisabled() throws Exception {
        final Application app = appWithoutBasicAuth();
        run(app, ApplicationController.class, controller -> {
            final Result result = controller.index();
            assertThat(result.status()).isLessThan(Http.Status.BAD_REQUEST);
        });
    }

    @Ignore
    @Test
    public void unauthorizedWhenBasicAuthEnabled() throws Exception {
        final Application app = appWithBasicAuth(BasicAuth.of("My Realm", "username:password"));
        run(app, ApplicationController.class, controller -> {
            final Result result = controller.index();
            assertThat(result.status()).isEqualTo(Http.Status.UNAUTHORIZED);
        });
    }

    private Application appWithoutBasicAuth() {
        return appWithBasicAuth(null);
    }

    private Application appWithBasicAuth(final BasicAuth basicAuth) {
        return app(new BasicAuthTestModule(basicAuth));
    }
}
