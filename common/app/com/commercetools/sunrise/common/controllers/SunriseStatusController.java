package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.cache.NoCache;
import io.sphere.sdk.client.SphereClient;
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
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletionStage;

/**
 * Controller for health and general status report.
 */
@NoCache
public abstract class SunriseStatusController extends SunriseFrameworkController {

    protected static final Logger logger = LoggerFactory.getLogger(SunriseStatusController.class);

    @Inject
    private Injector injector;
    @Inject
    private SphereClient sphere;

    @Override
    public Set<String> getFrameworkTags() {
        return Collections.emptySet();
    }

    public Result showVersion() throws IOException {
        final Application application = injector.instanceOf(Application.class);
        final InputStream versionAsStream = application.resourceAsStream("internal/version.json");
        final String versionAsString = IOUtils.toString(versionAsStream, StandardCharsets.UTF_8);
        return ok(versionAsString).as(Http.MimeTypes.JSON);
    }

    public CompletionStage<Result> showHealth() throws IOException {
        final ProductProjectionSearch productRequest = ProductProjectionSearch.ofCurrent().withLimit(1);
        return sphere.execute(productRequest)
                .thenApplyAsync(this::renderGoodHealthStatus, HttpExecution.defaultContext())
                .exceptionally(this::renderBadHealthStatus)
                .thenApply(result -> result.as(Http.MimeTypes.JSON));
    }

    protected Result renderGoodHealthStatus(final PagedSearchResult<ProductProjection> productResult) {
        final boolean containsProducts = !productResult.getResults().isEmpty();
        if (!containsProducts) {
            throw new RuntimeException("Cannot find any product!");
        }
        return ok(healthReportAsJson(true));
    }

    protected Result renderBadHealthStatus(final Throwable throwable) {
        logger.error("Could not fetch products", throwable);
        return status(Http.Status.SERVICE_UNAVAILABLE, healthReportAsJson(false));
    }

    protected String healthReportAsJson(final boolean healthy) {
        return "{\n" +
                "  \"self\" : {\n" +
                "    \"healthy\" : " + healthy + "\n" +
                "  }\n" +
                "}";
    }
}
