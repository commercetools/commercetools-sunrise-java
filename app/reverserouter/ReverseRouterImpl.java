package reverserouter;

import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.*;
import io.sphere.sdk.models.Base;
import play.mvc.Call;
import setupwidget.controllers.SetupReverseRouter;

import static demo.common.routes.*;
import static demo.myaccount.routes.*;
import static demo.productcatalog.routes.*;
import static demo.shoppingcart.routes.*;
import static setupwidget.controllers.routes.*;

public class ReverseRouterImpl extends Base implements ReverseRouter, HomeReverseRouter, ProductReverseRouter, CheckoutReverseRouter, AddressBookReverseRouter, MyPersonalDetailsReverseRouter, MyOrdersReverseRouter, SetupReverseRouter, CartReverseRouter, AuthenticationReverseRouter {

    @Override
    public Call themeAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return HomeController.show(languageTag);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
        return ProductOverviewController.searchProductsByCategorySlug(languageTag, categorySlug);
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
        return ProductDetailController.showProductBySlugAndSku(languageTag, productSlug, sku);
    }

    @Override
    public Call processSearchProductsForm(final String languageTag) {
        return ProductOverviewController.searchProductsBySearchTerm(languageTag);
    }

    @Override
    public Call showCart(final String languageTag) {
        return CartDetailController.show(languageTag);
    }

    @Override
    public Call processAddProductToCartForm(final String languageTag) {
        return AddProductToCartController.addProductToCart(languageTag);
    }

    @Override
    public Call processChangeLineItemQuantityForm(final String languageTag) {
        return ChangeLineItemQuantityController.changeLineItemQuantity(languageTag);
    }

    @Override
    public Call processDeleteLineItemForm(final String languageTag) {
        return RemoveLineItemController.removeLineItem(languageTag);
    }

    @Override
    public Call checkoutAddressesPageCall(final String languageTag) {
        return CheckoutAddressController.show(languageTag);
    }

    @Override
    public Call checkoutAddressesProcessFormCall(final String languageTag) {
        return CheckoutAddressController.process(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return CheckoutShippingController.show(languageTag);
    }

    @Override
    public Call checkoutShippingProcessFormCall(final String languageTag) {
        return CheckoutShippingController.process(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return CheckoutPaymentController.show(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessFormCall(final String languageTag) {
        return CheckoutPaymentController.process(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return CheckoutConfirmationController.show(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessFormCall(final String languageTag) {
        return CheckoutConfirmationController.process(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return CheckoutThankYouController.show(languageTag);
    }

    @Override
    public Call showLogInForm(final String languageTag) {
        return LogInController.show(languageTag);
    }

    @Override
    public Call processLogInForm(final String languageTag) {
        return LogInController.process(languageTag);
    }

    @Override
    public Call processSignUpForm(final String languageTag) {
        return SignUpController.process(languageTag);
    }

    @Override
    public Call processLogOut(final String languageTag) {
        return LogOutController.process(languageTag);
    }

    @Override
    public Call myPersonalDetailsPageCall(final String languageTag) {
        return MyPersonalDetailsController.show(languageTag);
    }

    @Override
    public Call myPersonalDetailsProcessFormCall(final String languageTag) {
        return MyPersonalDetailsController.process(languageTag);
    }

    @Override
    public Call addressBookCall(final String languageTag) {
        return AddressBookController.show(languageTag);
    }

    @Override
    public Call addAddressToAddressBookCall(final String languageTag) {
        return AddAddressController.show(languageTag);
    }

    @Override
    public Call addAddressToAddressBookProcessFormCall(final String languageTag) {
        return AddAddressController.process(languageTag);
    }

    @Override
    public Call changeAddressInAddressBookCall(final String languageTag, final String addressId) {
        return ChangeAddressController.show(languageTag, addressId);
    }

    @Override
    public Call changeAddressInAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return ChangeAddressController.process(languageTag, addressId);
    }

    @Override
    public Call removeAddressFromAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return RemoveAddressController.process(languageTag, addressId);
    }

    @Override
    public Call myOrderListPageCall(final String languageTag) {
        return MyOrderListController.show(languageTag);
    }

    @Override
    public Call myOrderDetailPageCall(final String languageTag, final String orderNumber) {
        return MyOrderDetailController.showByOrderNumber(languageTag, orderNumber);
    }

    @Override
    public Call processSetupFormCall() {
        return SetupController.processForm();
    }
}
