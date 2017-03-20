package com.commercetools.sunrise.common.healthcheck;

import com.commercetools.sunrise.test.TestableSphereClient;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;

import static com.commercetools.sunrise.test.JsonUtils.readCtpObject;
import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.contentAsString;

public class SunriseHealthCheckControllerTest {

    @Test
    public void showsHealthyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/products-search.json", ProductProjectionSearch.resultTypeReference());
        final SphereClient validResponseSphereClient = TestableSphereClient.ofResponse(result);
        assertThatRendersHealthyCtp(new HealthCheckControllerTest(validResponseSphereClient));
    }

    @Test
    public void showsEmptyCtp() throws Exception {
        final PagedSearchResult<ProductProjection> result = readCtpObject("data/empty-products-search.json", ProductProjectionSearch.resultTypeReference());
        final SphereClient emptyResponseSphereClient = TestableSphereClient.ofResponse(result);
        assertThatRendersUnhealthyCtp(new HealthCheckControllerTest(emptyResponseSphereClient));
    }

    @Test
    public void showsUnhealthyCtp() throws Exception {
        final SphereClient unhealthySphereClient = TestableSphereClient.ofUnhealthyPlatform();
        assertThatRendersUnhealthyCtp(new HealthCheckControllerTest(unhealthySphereClient));
    }

    @Test
    public void showsUnresponsiveCtp() throws Exception {
        final SphereClient unresponsiveSphereClient = TestableSphereClient.ofUnresponsivePlatform();
        assertThatRendersUnhealthyCtp(new HealthCheckControllerTest(unresponsiveSphereClient));
    }

    private void assertThatRendersHealthyCtp(final HealthCheckControllerTest controller) throws Exception {
        final Result result = controller.show().toCompletableFuture().join();
        assertThat(result.status()).isEqualTo(Http.Status.OK);
        assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : true");
    }

    private void assertThatRendersUnhealthyCtp(final HealthCheckControllerTest controller) throws Exception {
        final Result result = controller.show().toCompletableFuture().join();
        assertThat(result.status()).isEqualTo(Http.Status.SERVICE_UNAVAILABLE);
        assertThat(result.contentType()).contains(Http.MimeTypes.JSON);
        assertThat(contentAsString(result)).contains("\"healthy\" : false");
    }

    private static class HealthCheckControllerTest extends SunriseHealthCheckController {

        HealthCheckControllerTest(final SphereClient sphereClient) {
            super(sphereClient);
        }
    }
}