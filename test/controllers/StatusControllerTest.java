package controllers;

import common.controllers.WithSunriseApplication;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static common.JsonUtils.readCtpObject;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class StatusControllerTest extends WithSunriseApplication {
    private static final int ALLOWED_TIMEOUT = 1000;

    @Override
    @SuppressWarnings("unchecked")
    protected <T> CompletionStage<T> fakeSphereClientResponse(final SphereRequest<T> request) {
        final T result = (T) readCtpObject("data/products-search.json", ProductProjectionSearch.resultTypeReference());
        return CompletableFuture.completedFuture(result);
    }

    @Test
    public void showsHealthy() throws Exception {
        final Result result = app.injector().instanceOf(StatusController.class).health().get(ALLOWED_TIMEOUT);
        assertThat(result.status()).isEqualTo(200);
        assertThat(result.contentType()).isEqualTo(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : true");
    }

    @Ignore
    @Test
    public void showsNotHealthyOn() throws Exception {
        final Result result = app.injector().instanceOf(StatusController.class).health().get(ALLOWED_TIMEOUT);
        assertThat(result.status()).isGreaterThanOrEqualTo(500);
        assertThat(result.contentType()).isEqualTo(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : false");
    }
}
