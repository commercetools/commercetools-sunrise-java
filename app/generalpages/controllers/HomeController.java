package generalpages.controllers;

import play.inject.Injector;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import io.sphere.sdk.play.setupwidget.controllers.SetupController;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Front controller for the {@link ApplicationController} to enable a widget to set the SPHERE.IO credentials.
 */
@Singleton
public class HomeController extends Controller {
    private final Injector injector;
    private final SetupController setupController;

    @Inject
    public HomeController(final Injector injector, final SetupController setupController) {
        this.injector = injector;
        this.setupController = setupController;
    }

    public F.Promise<Result> index() {
        return setupController.handleOrFallback(() -> injector.instanceOf(ApplicationController.class).index());
    }

}
