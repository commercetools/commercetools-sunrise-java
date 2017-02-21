package controllers.common;

import com.commercetools.sunrise.framework.controllers.SunriseController;
import play.mvc.Result;

import javax.inject.Singleton;

/**
 * Controller for main web pages like imprint and contact.
 */
@Singleton
public final class ApplicationController extends SunriseController {

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }
}
