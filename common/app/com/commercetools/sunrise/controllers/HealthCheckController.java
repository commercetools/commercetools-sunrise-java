package com.commercetools.sunrise.controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

/**
 * Controller for health check report.
 */
@NoCache
@Singleton
public final class HealthCheckController extends Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckController.class);

    private final SphereClient sphereClient;

    @Inject
    HealthCheckController(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    public CompletionStage<Result> show() throws IOException {
        final ProductProjectionSearch productRequest = ProductProjectionSearch.ofCurrent()
                .withMarkingMatchingVariants(false)
                .withLimit(1);
        return sphereClient.execute(productRequest)
                .thenApplyAsync(this::renderGoodHealthStatus, HttpExecution.defaultContext())
                .exceptionally(this::renderBadHealthStatus)
                .thenApply(result -> result.as(Http.MimeTypes.JSON));
    }

    private Result renderGoodHealthStatus(final PagedSearchResult<ProductProjection> productResult) {
        final boolean containsNoProducts = productResult.getResults().isEmpty();
        if (containsNoProducts) {
            throw new RuntimeException("Cannot find any product!");
        }
        return ok(healthReportAsJson(true));
    }

    private Result renderBadHealthStatus(final Throwable throwable) {
        LOGGER.error("Could not fetch products", throwable);
        return status(Http.Status.SERVICE_UNAVAILABLE, healthReportAsJson(false));
    }

    private String healthReportAsJson(final boolean healthy) {
        return "{\n" +
                "  \"self\" : {\n" +
                "    \"healthy\" : " + healthy + "\n" +
                "  }\n" +
                "}";
    }
}
