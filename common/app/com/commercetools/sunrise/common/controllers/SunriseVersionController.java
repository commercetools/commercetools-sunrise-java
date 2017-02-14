package com.commercetools.sunrise.common.controllers;

import org.apache.commons.io.IOUtils;
import play.Application;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Controller for showing the version of the application.
 */
public abstract class SunriseVersionController extends Controller {

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
