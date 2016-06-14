package com.commercetools.sunrise.common;

import com.commercetools.sunrise.common.controllers.SunriseStatusController;
import com.commercetools.sunrise.common.controllers.TestableSphereClient;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.TimeUnit;

import static com.commercetools.sunrise.common.utils.JsonUtils.readCtpObject;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class SunriseStatusControllerTest extends WithSunriseApplication {

    @Test
    public void showsHealthyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = appWithSphereClient(TestableSphereClient.ofResponse(result));
        run(app, StatusControllerTest.class, this::rendersHealthyCtp);
    }

    @Test
    public void showsEmptyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/empty-products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = appWithSphereClient(TestableSphereClient.ofResponse(result));
        run(app, StatusControllerTest.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnhealthyCtp() throws Exception {
        final Application app = appWithSphereClient(TestableSphereClient.ofUnhealthyCtp());
        run(app, StatusControllerTest.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnresponsiveCtp() throws Exception {
        final Application app = appWithSphereClient(TestableSphereClient.ofUnresponsiveCtp());
        run(app, StatusControllerTest.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsVersion() throws Exception {
        run(app(), StatusControllerTest.class, controller -> {
            final Result result = controller.showVersion();
            assertThat(result.status()).isEqualTo(Http.Status.OK);
            assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
            assertThat(contentAsString(result)).contains("version").contains("build");
        });
    }

    private void rendersHealthyCtp(final SunriseStatusController controller) throws Exception {
        final Result result = controller.showHealth().toCompletableFuture().get(1, TimeUnit.SECONDS);
        assertThat(result.status()).isEqualTo(Http.Status.OK);
        assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : true");
    }

    private void rendersUnhealthyCtp(final SunriseStatusController controller) throws Exception {
        final Result result = controller.showHealth().toCompletableFuture().get(1, TimeUnit.SECONDS);
        assertThat(result.status()).isEqualTo(Http.Status.SERVICE_UNAVAILABLE);
        assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : false");
    }

    private Application appWithSphereClient(final SphereClient sphereClient) {
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(SphereClient.class).toInstance(sphereClient);
            }
        };
        return appBuilder(module).build();
    }

    private static class StatusControllerTest extends SunriseStatusController {

    }
}