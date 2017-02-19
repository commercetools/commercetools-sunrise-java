package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.common.healthcheck.SunriseHealthCheckController;
import com.commercetools.sunrise.pt.WithSunriseApplication;
import com.commercetools.sunrise.test.TestableSphereClient;
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

public class SunriseHealthCheckControllerTest extends WithSunriseApplication {

    @Test
    public void showsHealthyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = appWithSphereClient(TestableSphereClient.ofResponse(result));
        run(app, HealthCheckControllerTest.class, this::rendersHealthyCtp);
    }

    @Test
    public void showsEmptyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/empty-products-search.json", ProductProjectionSearch.resultTypeReference());
        final Application app = appWithSphereClient(TestableSphereClient.ofResponse(result));
        run(app, HealthCheckControllerTest.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnhealthyCtp() throws Exception {
        final Application app = appWithSphereClient(TestableSphereClient.ofUnhealthyCtp());
        run(app, HealthCheckControllerTest.class, this::rendersUnhealthyCtp);
    }

    @Test
    public void showsUnresponsiveCtp() throws Exception {
        final Application app = appWithSphereClient(TestableSphereClient.ofUnresponsiveCtp());
        run(app, HealthCheckControllerTest.class, this::rendersUnhealthyCtp);
    }

    private void rendersHealthyCtp(final HealthCheckControllerTest controller) throws Exception {
        final Result result = controller.show().toCompletableFuture().get(1, TimeUnit.SECONDS);
        assertThat(result.status()).isEqualTo(Http.Status.OK);
        assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : true");
    }

    private void rendersUnhealthyCtp(final HealthCheckControllerTest controller) throws Exception {
        final Result result = controller.show().toCompletableFuture().get(1, TimeUnit.SECONDS);
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

    private static class HealthCheckControllerTest extends SunriseHealthCheckController {

        public HealthCheckControllerTest(final SphereClient sphereClient) {
            super(sphereClient);
        }
    }
}