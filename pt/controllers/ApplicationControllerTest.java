package controllers;

import common.controllers.WithSunriseApplication;
import io.sphere.sdk.client.SphereRequest;
import productcatalog.controllers.HomeController;
import org.junit.Test;
import org.junit.Ignore;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class ApplicationControllerTest extends WithSunriseApplication {

    @Override
    protected <T> CompletionStage<T> fakeSphereClientResponse(final SphereRequest<T> request) {
        return null;
    }

    @Ignore
    @Test
    public void homeIsAlive() {
        final Result index = app.injector().instanceOf(HomeController.class).show("en").get(100);
        assertThat(index.status()).isEqualTo(200);
        assertThat(contentAsString(index)).contains("Sunrise Home");
    }
}