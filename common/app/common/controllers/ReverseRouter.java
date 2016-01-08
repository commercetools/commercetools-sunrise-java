package common.controllers;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

public interface ReverseRouter {

    Call home(final String languageTag);

    Call category(final String languageTag, final String categorySlug);

    Call search(final String languageTag);

    Call product(final String languageTag, final String productSlug, final String sku);

    Call productToCartForm(final String languageTag);

    Call showCheckoutAddressesForm(final String languageTag);

    Call processCheckoutAddressesForm(final String languageTag);

    Call showCheckoutShippingForm(final String languageTag);

    Call processCheckoutShippingForm(final String languageTag);

    Call showCheckoutPaymentForm(final String languageTag);

    Call processCheckoutPaymentForm(final String languageTag);

    Call showCheckoutConfirmationForm(final String languageTag);

    Call processCheckoutConfirmationForm(final String languageTag);

    Call designAssets(final String file);

    Call showCart(final String languageTag);

    Call showCheckoutThankyou(final String languageTag);

    Call processDeleteLineItem(final String languageTag);

    Call processChangeLineItemQuantity(final String languageTag);

    default Optional<Call> category(final Locale locale, final Category category) {
        return category.getSlug().find(locale)
                .map(slug -> category(locale.toLanguageTag(), slug));
    }

    default Optional<Call> product(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return product.getSlug().find(locale)
                .map(slug -> product(locale.toLanguageTag(), slug, productVariant.getSku()));
    }

    default String categoryUrlOrEmpty(final Locale locale, final Category category) {
        return category(locale, category).map(Call::url).orElse("");
    }

    default String productUrlOrEmpty(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return product(locale, product, productVariant).map(Call::url).orElse("");
    }
}
