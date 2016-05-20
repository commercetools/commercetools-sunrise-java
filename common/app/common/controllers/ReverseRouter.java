package common.controllers;

import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

public interface ReverseRouter {

    Call themeAssets(final String file);

    Call processChangeLanguageForm();

    Call processChangeCountryForm(final String languageTag);

    Call showHome(final String languageTag);

    Call showCategory(final String languageTag, final String categorySlug);

    Call processSearchProductsForm(final String languageTag);

    Call showProduct(final String languageTag, final String productSlug, final String sku);

    Call showCart(final String languageTag);

    Call processAddProductToCartForm(final String languageTag);

    Call processDeleteLineItemForm(final String languageTag);

    Call processChangeLineItemQuantityForm(final String languageTag);

    Call showCheckoutAddressesForm(final String languageTag);

    Call processCheckoutAddressesForm(final String languageTag);

    Call showCheckoutShippingForm(final String languageTag);

    Call processCheckoutShippingForm(final String languageTag);

    Call showCheckoutPaymentForm(final String languageTag);

    Call processCheckoutPaymentForm(final String languageTag);

    Call showCheckoutConfirmationForm(final String languageTag);

    Call processCheckoutConfirmationForm(final String languageTag);

    Call showCheckoutThankYou(final String languageTag);

    Call showLogInForm(final String languageTag);

    Call processLogInForm(final String languageTag);

    Call processSignUpForm(final String languageTag);

    Call processLogOut(final String languageTag);

    Call showMyPersonalDetails(final String languageTag);

    Call processMyPersonalDetailsForm(final String languageTag);

    Call showMyAddressBook(final String languageTag);

    Call processAddAddressToMyAddressBookForm(final String languageTag);

    Call processChangeAddressInMyAddressBookForm(final String languageTag);

    Call processRemoveAddressFromMyAddressBookForm(final String languageTag);

    Call showMyOrders(final String languageTag);

    Call showMyOrder(final String languageTag, final String orderNumber);


    default Optional<Call> showCategory(final Locale locale, final Category category) {
        return category.getSlug().find(locale)
                .map(slug -> showCategory(locale.toLanguageTag(), slug));
    }

    default Optional<Call> showProduct(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return product.getSlug().find(locale)
                .map(slug -> showProduct(locale.toLanguageTag(), slug, productVariant.getSku()));
    }

    default Optional<Call> showProduct(final Locale locale, final LineItem lineItem) {
        return Optional.ofNullable(lineItem.getProductSlug())
                .flatMap(slugs -> slugs.find(locale)
                        .map(slug -> showProduct(locale.toLanguageTag(), slug, lineItem.getVariant().getSku())));
    }

    default Optional<Call> showMyOrder(final Locale locale, final Order order) {
        return Optional.ofNullable(order.getOrderNumber())
                .map(orderNumber -> showMyOrder(locale.toLanguageTag(), orderNumber));
    }

    default String showCategoryUrlOrEmpty(final Locale locale, final Category category) {
        return showCategory(locale, category).map(Call::url).orElse("");
    }

    default String showProductUrlOrEmpty(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return showProduct(locale, product, productVariant).map(Call::url).orElse("");
    }

    default String showProductUrlOrEmpty(final Locale locale, final LineItem lineItem) {
        return showProduct(locale, lineItem).map(Call::url).orElse("");
    }

    default String showMyOrderUrlOrEmpty(final Locale locale, final Order order) {
        return showMyOrder(locale, order).map(Call::url).orElse("");
    }
}
