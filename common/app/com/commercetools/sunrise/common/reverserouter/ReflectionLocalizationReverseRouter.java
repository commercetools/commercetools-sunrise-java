package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionLocalizationReverseRouter extends AbstractReflectionReverseRouter implements LocalizationSimpleReverseRouter {

    private final ReverseCaller processChangeLanguageForm;
    private final ReverseCaller processChangeCountryForm;

    @Inject
    private ReflectionLocalizationReverseRouter(final ParsedRouteList parsedRouteList) {
        processChangeLanguageForm = getCallerForRoute(parsedRouteList, "processChangeLanguageForm");
        processChangeCountryForm = getCallerForRoute(parsedRouteList, "processChangeCountryForm");
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
