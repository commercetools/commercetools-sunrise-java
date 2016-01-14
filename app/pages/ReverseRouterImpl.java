package pages;

import common.controllers.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.mvc.Call;

import static productcatalog.home.routes.*;
import static productcatalog.productdetail.routes.*;
import static productcatalog.productoverview.routes.*;
import static shoppingcart.cartdetail.routes.*;
import static shoppingcart.checkout.address.routes.*;
import static shoppingcart.checkout.confirmation.routes.*;
import static shoppingcart.checkout.payment.routes.*;
import static shoppingcart.checkout.shipping.routes.*;
import static shoppingcart.checkout.thankyou.routes.*;

public class ReverseRouterImpl extends Base implements ReverseRouter {

    public ReverseRouterImpl() {
    }

    @Override
    public Call home(final String languageTag) {
        return HomeController.show(languageTag);
    }

    @Override
    public Call changeLanguage() {
        return controllers.routes.ApplicationController.changeLanguage();
    }

    @Override
    public Call category(final String languageTag, final String categorySlug) {
        return ProductOverviewPageController.show(languageTag, 1, categorySlug);
    }

    @Override
    public Call search(final String languageTag) {
        return ProductOverviewPageController.search(languageTag, 1);
    }

    @Override
    public Call product(final String languageTag, final String productSlug, final String sku) {
        return ProductDetailPageController.show(languageTag, productSlug, sku);
    }

    @Override
    public Call productToCartForm(final String languageTag) {
        return CartDetailPageController.addToCart(languageTag);
    }

    @Override
    public Call showCheckoutAddressesForm(final String languageTag) {
        return CheckoutAddressController.show(languageTag);
    }

    @Override
    public Call processCheckoutAddressesForm(final String languageTag) {
        return CheckoutAddressController.process(languageTag);
    }

    @Override
    public Call showCheckoutShippingForm(final String languageTag) {
        return CheckoutShippingController.show(languageTag);
    }

    @Override
    public Call processCheckoutShippingForm(final String languageTag) {
        return CheckoutShippingController.process(languageTag);
    }

    @Override
    public Call showCheckoutPaymentForm(final String languageTag) {
        return CheckoutPaymentController.show(languageTag);
    }

    @Override
    public Call processCheckoutPaymentForm(final String languageTag) {
        return CheckoutPaymentController.process(languageTag);
    }

    @Override
    public Call showCheckoutConfirmationForm(final String languageTag) {
        return CheckoutConfirmationController.show(languageTag);
    }

    @Override
    public Call processCheckoutConfirmationForm(final String languageTag) {
        return CheckoutConfirmationController.process(languageTag);
    }

    @Override
    public Call designAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }

    @Override
    public Call showCart(final String languageTag) {
        return CartDetailPageController.show(languageTag);
    }

    @Override
    public Call processDeleteLineItem(final String languageTag) {
        return CartDetailPageController.processRemoveLineItem(languageTag);
    }

    @Override
    public Call processChangeLineItemQuantity(final String languageTag) {
        return CartDetailPageController.processChangeLineItemQuantity(languageTag);
    }

    @Override
    public Call showCheckoutThankyou(final String languageTag) {
        return CheckoutThankyouController.show(languageTag);
    }
}
