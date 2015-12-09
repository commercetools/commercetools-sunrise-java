package controllers;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import org.apache.commons.io.IOUtils;
import play.Application;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Controller for health report.
 */
@Singleton
public class StatusController extends SunriseController {
    private final Application application;

    @Inject
    public StatusController(final ControllerDependency controllerDependency, final Application application) {
        super(controllerDependency);
        this.application = application;
    }

    public Result version() throws IOException {
        final String jsonString =
                IOUtils.toString(application.resourceAsStream("internal/version.json"), StandardCharsets.UTF_8);
        return ok(jsonString).as(Http.MimeTypes.JSON);
    }

    public F.Promise<Result> health() throws IOException {
        return sphere().execute(ProductProjectionSearch.ofCurrent().withLimit(1))
                .map(result -> {
                    final boolean ok = !result.getResults().isEmpty();
                    if (!ok) {
                        throw new RuntimeException("cannot fetch any product");
                    }
                    return ok("{\n" +
                            "  \"self\" : {\n" +
                            "    \"healthy\" : true\n" +
                            "  }\n" +
                            "}");
                })
                .recover(e -> status(Http.Status.SERVICE_UNAVAILABLE, "{\n" +
                        "  \"self\" : {\n" +
                        "    \"healthy\" : false\n" +
                        "  }\n" +
                        "}"))
                .map(r -> r.as(Http.MimeTypes.JSON));
    }
}
