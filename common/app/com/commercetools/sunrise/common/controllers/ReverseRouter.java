package com.commercetools.sunrise.common.controllers;

import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

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

    Call showMyPersonalDetailsForm(final String languageTag);

    Call processMyPersonalDetailsForm(final String languageTag);

    Call showMyOrders(final String languageTag);

    Call showMyOrder(final String languageTag, final String orderNumber);

    default Optional<Call> showMyOrder(final Locale locale, final Order order) {
        return Optional.ofNullable(order.getOrderNumber())
                .map(orderNumber -> showMyOrder(locale.toLanguageTag(), orderNumber));
    }

    default String showMyOrderUrlOrEmpty(final Locale locale, final Order order) {
        return showMyOrder(locale, order).map(Call::url).orElse("");
    }
}
