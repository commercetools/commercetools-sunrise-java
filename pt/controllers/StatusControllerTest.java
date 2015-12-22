package controllers;

import common.controllers.TestableSphereClient;
import common.controllers.WithSunriseApplication;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;

import static common.JsonUtils.readCtpObject;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class StatusControllerTest extends WithSunriseApplication {

    @Test
    public void showsHealthyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = applicationBuilder(TestableSphereClient.ofResponse(result)).build();
        run(app, StatusController.class, this::rendersHealthyCtp);
    }

    @Test
    public void showsEmptyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/empty-products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = applicationBuilder(TestableSphereClient.ofResponse(result)).build();
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnhealthyCtp() throws Exception {
        final Application app = applicationBuilder(TestableSphereClient.ofUnhealthyCtp()).build();
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnresponsiveCtp() throws Exception {
        final Application app = applicationBuilder(TestableSphereClient.ofUnresponsiveCtp()).build();
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsVersion() throws Exception {
        final Application app = applicationBuilder(TestableSphereClient.ofEmptyResponse()).build();
        run(app, StatusController.class, controller -> {
            final Result result = controller.version();
            assertThat(result.status()).isEqualTo(Http.Status.OK);
            assertThat(result.contentType()).isEqualTo(Http.MimeTypes.JSON);
            assertThat(contentAsString(result)).contains("version").contains("build");
        });
    }

    private void rendersHealthyCtp(final StatusController controller) throws IOException {
        final Result result = controller.health().get(ALLOWED_TIMEOUT);
        assertThat(result.status()).isEqualTo(Http.Status.OK);
        assertThat(result.contentType()).isEqualTo(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : true");
    }

    private void rendersUnhealthyCtp(final StatusController controller) throws IOException {
        final Result result = controller.health().get(ALLOWED_TIMEOUT);
        assertThat(result.status()).isEqualTo(Http.Status.SERVICE_UNAVAILABLE);
        assertThat(result.contentType()).isEqualTo(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : false");
    }
}