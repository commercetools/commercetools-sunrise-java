package controllers;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import productcatalog.home.HomeController;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class ApplicationControllerTest extends WithSunriseApplication {

    @Test
    public void homeIsAlive() {
        setContext(requestBuilder().build());
        run(app(), HomeController.class, controller -> {
            final Result result = controller.show("en").toCompletableFuture().get(ALLOWED_TIMEOUT, TimeUnit.MILLISECONDS);
            assertThat(result.status()).isEqualTo(Http.Status.OK);
            assertThat(contentAsString(result)).isNullOrEmpty();
        });
    }
}