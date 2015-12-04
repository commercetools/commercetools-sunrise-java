package controllers;

import common.controllers.WithSunriseApplication;
import productcatalog.controllers.HomeController;
import io.sphere.sdk.client.HttpRequestIntent;
import org.junit.Test;
import org.junit.Ignore;
import play.mvc.Result;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class ApplicationControllerTest extends WithSunriseApplication {

    @Override
    protected Function<HttpRequestIntent, Object> getTestDoubleBehavior() {
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