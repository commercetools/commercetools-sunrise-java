package controllers;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Controller for main web pages like index, imprint and contact.
 */
@Singleton
public final class ApplicationController extends SunriseController {

    @Inject
    public ApplicationController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    public F.Promise<Result> index() {
        return F.Promise.pure(ok("Sunrise Home"));
    }
}
