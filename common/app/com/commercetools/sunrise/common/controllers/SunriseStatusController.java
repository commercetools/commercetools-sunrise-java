package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.cache.NoCache;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Application;
import play.inject.Injector;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

/**
 * Controller for health report.
 */
@NoCache
public abstract class SunriseStatusController extends SunriseFrameworkController {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseStatusController.class);

    @Inject
    private Injector injector;

    public Result version() throws IOException {
        final Application application = injector.instanceOf(Application.class);
        final InputStream versionAsStream = application.resourceAsStream("internal/version.json");
        final String versionAsString = IOUtils.toString(versionAsStream, StandardCharsets.UTF_8);
        return ok(versionAsString).as(Http.MimeTypes.JSON);
    }

    public CompletionStage<Result> health() throws IOException {
        final ProductProjectionSearch productRequest = ProductProjectionSearch.ofCurrent().withLimit(1);
        return sphere().execute(productRequest)
                .thenApplyAsync(this::renderGoodHealthStatus, HttpExecution.defaultContext())
                .exceptionally(this::renderBadHealthStatus)
                .thenApply(result -> result.as(Http.MimeTypes.JSON));
    }

    private Result renderGoodHealthStatus(final PagedSearchResult<ProductProjection> productResult) {
        final boolean containsProducts = !productResult.getResults().isEmpty();
        if (!containsProducts) {
            throw new RuntimeException("Cannot find any product!");
        }
        return ok(healthJson(true));
    }

    private Result renderBadHealthStatus(final Throwable t) {
        logger.error("Could not fetch products", t);
        return status(Http.Status.SERVICE_UNAVAILABLE, healthJson(false));
    }

    private String healthJson(final boolean healthy) {
        return "{\n" +
                "  \"self\" : {\n" +
                "    \"healthy\" : " + healthy + "\n" +
                "  }\n" +
                "}";
    }
}
