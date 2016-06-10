package controllers;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.inject.RequestScoped;
import play.data.FormFactory;
import play.inject.Injector;
import play.mvc.Controller;
import setupwidget.controllers.SetupController;

import javax.inject.Inject;

/**
 * Controller for main web pages like index, imprint and contact.
 */
@RequestScoped
public final class ApplicationController extends Controller {
    private final Injector injector;
    private final SetupController setupController;

    @Inject
    private UserContext userContext;

    @Inject
    public ApplicationController(final Injector injector, final SetupController setupController, final FormFactory formFactory) {
        this.injector = injector;
        this.setupController = setupController;
    }

//    public Result index() {
//        return setupController.handleOrFallback(() -> redirectToHomePage(defaultLanguage()));
//    }
}
