package controllers;

import common.contexts.ProjectContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.data.Form;
import play.data.validation.Constraints;
import play.inject.Injector;
import play.mvc.Controller;
import play.mvc.Result;
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

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }

    public Result index() {
        return setupController.handleOrFallback(() -> redirectToHomePage(defaultLanguage()));
    }

    public Result changeLanguage() {
        final Form<LanguageFormData> languageFilledForm = Form.form(LanguageFormData.class).bindFromRequest();
        final String languageTag = (languageFilledForm.hasErrors())? defaultLanguage() : languageFilledForm.get().getLanguage();
        return redirectToHomePage(languageTag);
    }

    private Result redirectToHomePage(final String languageTag) {
        final ReverseRouter reverseRouter = injector.instanceOf(ReverseRouter.class);
        return redirect(reverseRouter.home(languageTag));
    }

    private String defaultLanguage() {
        final ProjectContext projectContext = injector.instanceOf(ProjectContext.class);
        return projectContext.defaultLocale().toLanguageTag();
    }

    public static class LanguageFormData extends Base {
        @Constraints.Required
        private String language;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(final String language) {
            this.language = language;
        }
    }
}
