package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.WithSunriseApplication;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class HomePageControllerTest extends WithSunriseApplication {

    @Test
    public void homeIsAlive() {
        setContext(requestBuilder().build());
        run(app(), SunriseHomePageController.class, controller -> {
            final Result result = controller.show("en").toCompletableFuture().get(ALLOWED_TIMEOUT, TimeUnit.MILLISECONDS);
            assertThat(result.status()).isEqualTo(Http.Status.OK);
            assertThat(contentAsString(result)).isNullOrEmpty();
        });
    }
}