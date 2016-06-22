package demo.common;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controller for main web pages like imprint and contact.
 */
public class ApplicationController extends Controller {

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }
}
