package com.commercetools.sunrise.common.version;

import com.commercetools.sunrise.framework.controllers.SunriseController;
import org.apache.commons.io.IOUtils;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Controller for showing the version of the application.
 */
public abstract class SunriseVersionController extends SunriseController {

    private final Application application;

    protected SunriseVersionController(final Application application) {
        this.application = application;
    }

    public Result show() throws IOException {
        final InputStream versionAsStream = application.resourceAsStream("internal/version.json");
        final String versionAsString = IOUtils.toString(versionAsStream, StandardCharsets.UTF_8);
        return ok(versionAsString).as(Http.MimeTypes.JSON);
    }
}
