package controllers.common;

import com.commercetools.sunrise.framework.controllers.SunriseController;
import play.mvc.Result;

import javax.inject.Singleton;

/**
 * Controller which only purpose is to enable both "some-url/" and "some-url" paths.
 */
@Singleton
public final class ApplicationController extends SunriseController {

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }
}
