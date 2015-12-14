package controllers;

import common.controllers.FakeSphereClient;
import common.controllers.WithSunriseApplication;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.junit.Ignore;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;

import static common.JsonUtils.readCtpObject;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class StatusControllerTest extends WithSunriseApplication {
    private static final int ALLOWED_TIMEOUT = 1000;

    @Test
    public void showsHealthyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = applicationBuilder(FakeSphereClient.ofResponse(result)).build();
        run(app, StatusController.class, this::rendersHealthyCtp);
    }

    @Test
    public void showsEmptyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/empty-products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = applicationBuilder(FakeSphereClient.ofResponse(result)).build();
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnhealthyCtp() throws Exception {
        final Application app = applicationBuilder(FakeSphereClient.ofUnhealthyCtp()).build();
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Ignore
    @Test
    public void showsUnresponsiveCtp() throws Exception {
        final Application app = applicationBuilder(FakeSphereClient.ofUnresponsiveCtp()).build();
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    private void rendersHealthyCtp(final StatusController controller) {
        try {
            final Result result = controller.health().get(ALLOWED_TIMEOUT);
            assertThat(result.status()).isEqualTo(200);
            assertThat(result.contentType()).isEqualTo(Http.MimeTypes.JSON);
            assertThat(contentAsString(result)).contains("\"healthy\" : true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rendersUnhealthyCtp(final StatusController controller) {
        try {
            final Result result = controller.health().get(ALLOWED_TIMEOUT);
            assertThat(result.status()).isGreaterThanOrEqualTo(500);
            assertThat(result.contentType()).isEqualTo(Http.MimeTypes.JSON);
            assertThat(contentAsString(result)).contains("\"healthy\" : false");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}