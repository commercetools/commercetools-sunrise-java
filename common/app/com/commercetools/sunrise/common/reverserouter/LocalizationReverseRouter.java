package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public interface LocalizationReverseRouter {
    Call processChangeLanguageForm();

    Call processChangeCountryForm(final String languageTag);
}
