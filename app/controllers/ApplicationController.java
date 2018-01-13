package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Singleton;

/**
 * Controller which only purpose is to enable both "some-url/" and "some-url" paths.
 */
@Singleton
public final class ApplicationController extends Controller {

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }
}
