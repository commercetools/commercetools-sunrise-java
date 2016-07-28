package com.commercetools.sunrise.common.controllers;

import play.mvc.Call;

public interface ReverseRouter {

    Call themeAssets(final String file);

    Call processChangeLanguageForm();

    Call processChangeCountryForm(final String languageTag);

    Call processSearchProductsForm(final String languageTag);
}
