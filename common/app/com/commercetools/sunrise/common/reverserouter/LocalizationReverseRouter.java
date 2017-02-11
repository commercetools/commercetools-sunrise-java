package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionLocalizationLocalizedReverseRouter.class)
public interface LocalizationReverseRouter extends LocalizationSimpleReverseRouter, LocalizedReverseRouter {

    default Call processChangeCountryForm() {
        return processChangeCountryForm(languageTag());
    }
}
