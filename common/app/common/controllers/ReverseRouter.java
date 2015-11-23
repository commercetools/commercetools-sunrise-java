package common.controllers;

import play.mvc.Call;

public interface ReverseRouter {

    Call home(final String languageTag);

    Call category(final String languageTag, final String categorySlug, final int page);

    Call category(final String languageTag, final String categorySlug);

    Call search(final String languageTag, final String searchTerm, final int page);

    Call search(final String languageTag, final String searchTerm);

    Call product(final String locale, final String productSlug, final String sku);

    Call productVariantToCartForm(final String languageTag);

    Call showCheckoutShippingForm(final String language);

    Call processCheckoutShippingForm(final String language);

    Call showCheckoutPaymentForm(String language);

    Call processCheckoutPaymentForm(String language);

    Call showCheckoutConfirmationForm(final String language);

    Call processCheckoutConfirmationForm(final String language);

    Call designAssets(final String file);

    Call showCart(final String language);
}
