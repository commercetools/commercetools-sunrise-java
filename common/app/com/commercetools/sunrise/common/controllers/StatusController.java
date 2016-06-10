package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.actions.NoCache;
import com.commercetools.sunrise.common.controllers.ControllerDependency;
import com.commercetools.sunrise.common.controllers.SunriseController;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.apache.commons.io.IOUtils;
import play.Application;
import play.Logger;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

/**
 * Controller for health report.
 */
@NoCache
@Singleton
public class StatusController extends SunriseController {
    private final Application application;

    @Inject
    public StatusController(final ControllerDependency controllerDependency, final Application application) {
        super(controllerDependency);
        this.application = application;
    }

    public Result version() throws IOException {
        final InputStream versionAsStream = application.resourceAsStream("internal/version.json");
        final String versionAsString = IOUtils.toString(versionAsStream, StandardCharsets.UTF_8);
        return ok(versionAsString).as(Http.MimeTypes.JSON);
    }

    public CompletionStage<Result> health() throws IOException {
        final ProductProjectionSearch productRequest = ProductProjectionSearch.ofCurrent().withLimit(1);
        final CompletionStage<PagedSearchResult<ProductProjection>> searchResultStage = sphere().execute(productRequest);
        return searchResultStage
                .thenApplyAsync(this::renderGoodHealthStatus, HttpExecution.defaultContext())
                .exceptionally(this::renderBadHealthStatus)
                .thenApply(r -> r.as(Http.MimeTypes.JSON));
    }

    private Result renderGoodHealthStatus(final PagedSearchResult<ProductProjection> productResult) {
        final boolean containsProducts = !productResult.getResults().isEmpty();
        if (!containsProducts) {
            throw new RuntimeException("Cannot find any product!");
        }
        return ok(healthJson(true));
    }

    private Result renderBadHealthStatus(final Throwable t) {
        Logger.error("Could not fetch products", t);
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
