package com.commercetools.sunrise.controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import org.apache.commons.io.IOUtils;
import play.Application;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Controller for showing the version of the application.
 */
@NoCache
@Singleton
public final class VersionController extends Controller {

    private final Application application;

    @Inject
    VersionController(final Application application) {
        this.application = application;
    }

    public Result show() throws IOException {
        final InputStream versionAsStream = application.resourceAsStream("internal/version.json");
        final String versionAsString = IOUtils.toString(versionAsStream, StandardCharsets.UTF_8);
        return ok(versionAsString).as(Http.MimeTypes.JSON);
    }
}
