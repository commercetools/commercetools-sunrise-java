package controllers.common;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Singleton;

/**
 * Controller for main web pages like imprint and contact.
 */
@Singleton
public final class ApplicationController extends Controller {

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }
}
