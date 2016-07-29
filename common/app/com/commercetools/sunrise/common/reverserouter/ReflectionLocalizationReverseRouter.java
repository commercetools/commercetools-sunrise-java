package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionLocalizationReverseRouter extends ReflectionReverseRouterBase implements LocalizationReverseRouter {

    private ReverseCaller processChangeLanguageForm;
    private ReverseCaller processChangeCountryForm;

    @Inject
    private void setRoutes(final ParsedRoutes parsedRoutes) {
        processChangeLanguageForm = getCallerForRoute(parsedRoutes, "processChangeLanguageForm");
        processChangeCountryForm = getCallerForRoute(parsedRoutes, "processChangeCountryForm");
    }


    @Override
    public Call processChangeLanguageForm() {
        return processChangeLanguageForm.call();
    }

    @Override
    public Call processChangeCountryForm(final String languageTag) {
        return processChangeCountryForm.call(languageTag);
    }
}
