package controllers;

import common.controllers.TestableSphereClient;
import ctpclient.CtpClientTestModule;
import io.sphere.sdk.client.SphereClient;
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
        final Application app = app(TestableSphereClient.ofResponse(result));
        run(app, StatusController.class, this::rendersHealthyCtp);
    }

    @Test
    public void showsEmptyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/empty-products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = app(TestableSphereClient.ofResponse(result));
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnhealthyCtp() throws Exception {
        final Application app = app(TestableSphereClient.ofUnhealthyCtp());
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnresponsiveCtp() throws Exception {
        final Application app = app(TestableSphereClient.ofUnresponsiveCtp());
        run(app, StatusController.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsVersion() throws Exception {
        run(app(), StatusController.class, controller -> {
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

    private static Application app(final SphereClient sphereClient) {
        return app(new CtpClientTestModule(sphereClient));
    }
}