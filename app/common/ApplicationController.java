package common;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.inject.RequestScoped;
import io.sphere.sdk.models.Base;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.inject.Injector;
import play.mvc.Controller;
import play.mvc.Result;
import setupwidget.controllers.SetupController;

import javax.inject.Inject;
import javax.inject.Singleton;

import static common.controllers.SunriseController.SESSION_COUNTRY;

/**
 * Controller for main web pages like index, imprint and contact.
 */
@RequestScoped
public final class ApplicationController extends Controller {
    private final Injector injector;
    private final SetupController setupController;
    private Form<LanguageFormData> languageForm;
    private Form<CountryFormData> countryForm;

    @Inject
    private UserContext userContext;

    @Inject
    public ApplicationController(final Injector injector, final SetupController setupController, final FormFactory formFactory) {
        this.injector = injector;
        this.setupController = setupController;
        this.languageForm = formFactory.form(LanguageFormData.class);
        this.countryForm = formFactory.form(CountryFormData.class);
    }

    public Result untrail(final String path) {
        return movedPermanently("/" + path);
    }

    public Result index() {
        return setupController.handleOrFallback(() -> redirectToHomePage(defaultLanguage()));
    }

    public Result changeLanguage() {
        final Form<LanguageFormData> boundForm = languageForm.bindFromRequest();
        final String languageTag = boundForm.hasErrors() ? defaultLanguage() : boundForm.get().getLanguage();
        return redirectToHomePage(languageTag);
    }

    public Result changeCountry(final String languageTag) {
        final Form<CountryFormData> boundForm = countryForm.bindFromRequest();
        final String country = boundForm.hasErrors() ? defaultCountry() : boundForm.get().getCountry();
        session(SESSION_COUNTRY, country);
        Logger.debug("Changed country: " + session().toString());
        return redirectToHomePage(languageTag);
    }

    private Result redirectToHomePage(final String languageTag) {
        final ReverseRouter reverseRouter = injector.instanceOf(ReverseRouter.class);
        return redirect(reverseRouter.showHome(languageTag));
    }

    private String defaultLanguage() {
        final ProjectContext projectContext = injector.instanceOf(ProjectContext.class);
        return projectContext.defaultLocale().toLanguageTag();
    }

    private String defaultCountry() {
        final ProjectContext projectContext = injector.instanceOf(ProjectContext.class);
        return projectContext.defaultCountry().getAlpha2();
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

    public static class CountryFormData extends Base {
        @Constraints.Required
        private String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(final String country) {
            this.country = country;
        }
    }
}
