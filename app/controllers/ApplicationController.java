package controllers;

import play.inject.Injector;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import productcatalog.controllers.HomeController;
import setupwidget.controllers.SetupController;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Controller for main web pages like index, imprint and contact.
 */
@Singleton
public final class ApplicationController extends Controller {
    private final Injector injector;
    private final SetupController setupController;

    @Inject
    public ApplicationController(final Injector injector, final SetupController setupController) {
        this.injector = injector;
        this.setupController = setupController;
    }

    public F.Promise<Result> index() {
        return setupController.handleOrFallback(() -> {
            final HomeController homeController = injector.instanceOf(HomeController.class);
            final String languageTag = lang().code();
            return homeController.show(languageTag);
        });
    }

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }
}
