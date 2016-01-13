package controllers;

import common.controllers.TestableSphereClient;
import common.controllers.WithSunriseApplication;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import productcatalog.home.HomeController;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class ApplicationControllerTest extends WithSunriseApplication {

    @Test
    public void homeIsAlive() {
        setContext(requestBuilder().build());
        final Application app = applicationBuilder(TestableSphereClient.ofEmptyResponse()).build();
        run(app, HomeController.class, controller -> {
            final Result result = controller.show("en").get(ALLOWED_TIMEOUT);
            assertThat(result.status()).isEqualTo(Http.Status.OK);
            assertThat(contentAsString(result)).isNullOrEmpty();
        });
    }
}