package com.commercetools.sunrise.common.controllers;

import play.mvc.Call;

public interface ReverseRouter {

    Call themeAssets(final String file);

    Call processChangeLanguageForm();

    Call processChangeCountryForm(final String languageTag);

    Call processSearchProductsForm(final String languageTag);

    Call showCart(final String languageTag);

    Call processAddProductToCartForm(final String languageTag);

    Call processDeleteLineItemForm(final String languageTag);

    Call processChangeLineItemQuantityForm(final String languageTag);

    Call showLogInForm(final String languageTag);

    Call processLogInForm(final String languageTag);

    Call processSignUpForm(final String languageTag);

    Call processLogOut(final String languageTag);
}
