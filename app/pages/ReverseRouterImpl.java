package pages;

import common.controllers.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.mvc.Call;

import static productcatalog.controllers.routes.*;
import static shoppingcart.routes.*;

public class ReverseRouterImpl extends Base implements ReverseRouter {

    public ReverseRouterImpl() {
    }

    @Override
    public Call home(final String languageTag) {
        return new Call() {
            @Override
            public String url() {
                return "/";
            }

            @Override
            public String method() {
                return "GET";
            }

            @Override
            public String fragment() {
                return null;
            }
        };
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
    public Call showCheckoutAddressesForm(final String language) {
        return CheckoutAddressController.show(language);
    }

    @Override
    public Call processCheckoutAddressesForm(final String language) {
        return CheckoutAddressController.process(language);
    }

    @Override
    public Call showCheckoutShippingForm(final String language) {
        return CheckoutShippingController.show(language);
    }

    @Override
    public Call processCheckoutShippingForm(final String language) {
        return CheckoutShippingController.process(language);
    }

    @Override
    public Call showCheckoutPaymentForm(final String language) {
        return CheckoutPaymentController.show(language);
    }

    @Override
    public Call processCheckoutPaymentForm(final String language) {
        return CheckoutPaymentController.process(language);
    }

    @Override
    public Call showCheckoutConfirmationForm(final String language) {
        return CheckoutConfirmationController.show(language);
    }

    @Override
    public Call processCheckoutConfirmationForm(final String language) {
        return CheckoutConfirmationController.process(language);
    }

    @Override
    public Call designAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }

    @Override
    public Call showCart(final String language) {
        return CartDetailPageController.show(language);
    }

    @Override
    public Call processDeleteLineItem(final String language) {
        return CartDetailPageController.processRemoveLineItem(language);
    }

    @Override
    public Call processChangeLineItemQuantity(final String language) {
        return CartDetailPageController.processChangeLineItemQuantity(language);
    }

    @Override
    public Call showCheckoutThankyou(final String language) {
        return CheckoutThankyouController.show(language);
    }
}
