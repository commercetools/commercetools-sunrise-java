package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.inject.Injector;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public abstract class SunriseLocalizationController extends SunriseFrameworkController {
    public static final String SESSION_COUNTRY = "countryCode";

    protected static final Logger logger = LoggerFactory.getLogger(SunriseLocalizationController.class);

    @Inject
    private FormFactory formFactory;
    @Inject
    private Injector injector;

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("localization-controller", "country", "language"));
    }

    public Result changeLanguage() {
        final Form<LanguageFormData> languageForm = formFactory.form(LanguageFormData.class).bindFromRequest();
        final String languageTag = languageForm.hasErrors() ? defaultLanguage() : languageForm.get().getLanguage();
        logger.debug("Changed language: " + languageTag);
        return redirectToHomePage(languageTag);
    }

    public Result changeCountry(final String languageTag) {
        final Form<CountryFormData> boundForm = formFactory.form(CountryFormData.class).bindFromRequest();
        final String country = boundForm.hasErrors() ? defaultCountry() : boundForm.get().getCountry();
        session(SESSION_COUNTRY, country);
        logger.debug("Changed country: " + country);
        return redirectToHomePage(languageTag);
    }

    private Result redirectToHomePage(final String languageTag) {
        final HomeReverseRouter homeReverseRouter = injector.instanceOf(HomeReverseRouter.class);
        return redirect(homeReverseRouter.homePageCall(languageTag));
    }

    private String defaultLanguage() {
        final ProjectContext projectContext = injector.instanceOf(ProjectContext.class);
        return projectContext.defaultLocale().toLanguageTag();
    }

    private String defaultCountry() {
        final ProjectContext projectContext = injector.instanceOf(ProjectContext.class);
        return projectContext.defaultCountry().getAlpha2();
    }

}
