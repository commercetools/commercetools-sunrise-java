package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionLocalizationReverseRouter.class)
public interface LocalizationReverseRouter {
    Call processChangeLanguageForm();

    Call processChangeCountryForm(final String languageTag);
}
