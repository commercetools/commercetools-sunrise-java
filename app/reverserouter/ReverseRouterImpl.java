package reverserouter;

import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import io.sphere.sdk.models.Base;
import play.mvc.Call;
import setupwidget.controllers.SetupReverseRouter;

import static demo.common.routes.*;
import static demo.productcatalog.routes.*;
import static demo.myaccount.routes.*;
import static demo.shoppingcart.routes.*;
import static setupwidget.controllers.routes.*;

public class ReverseRouterImpl extends Base implements ReverseRouter, HomeReverseRouter, ProductReverseRouter, CheckoutReverseRouter, AddressBookReverseRouter, SetupReverseRouter {

    @Override
    public Call themeAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }

    @Override
    public Call processChangeLanguageForm() {
        return LocalizationController.changeLanguage();
    }

    @Override
    public Call processChangeCountryForm(final String languageTag) {
        return LocalizationController.changeCountry(languageTag);
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return HomePageController.show(languageTag);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
        return ProductOverviewPageController.searchProductsByCategorySlug(languageTag, categorySlug);
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
        return ProductDetailPageController.showProductBySlugAndSku(languageTag, productSlug, sku);
    }

    @Override
    public Call processSearchProductsForm(final String languageTag) {
        return ProductOverviewPageController.searchProductsBySearchTerm(languageTag);
    }

    @Override
    public Call showCart(final String languageTag) {
        return CartDetailPageController.show(languageTag);
    }

    @Override
    public Call processAddProductToCartForm(final String languageTag) {
        return CartDetailPageController.addProductToCart(languageTag);
    }

    @Override
    public Call processChangeLineItemQuantityForm(final String languageTag) {
        return CartDetailPageController.changeLineItemQuantity(languageTag);
    }

    @Override
    public Call processDeleteLineItemForm(final String languageTag) {
        return CartDetailPageController.removeLineItem(languageTag);
    }

    @Override
    public Call checkoutAddressesPageCall(final String languageTag) {
        return CheckoutAddressPageController.show(languageTag);
    }

    @Override
    public Call checkoutAddressesProcessFormCall(final String languageTag) {
        return CheckoutAddressPageController.process(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return CheckoutShippingPageController.show(languageTag);
    }

    @Override
    public Call checkoutShippingProcessFormCall(final String languageTag) {
        return CheckoutShippingPageController.process(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return CheckoutPaymentPageController.show(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessFormCall(final String languageTag) {
        return CheckoutPaymentPageController.process(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return CheckoutConfirmationPageController.show(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessFormCall(final String languageTag) {
        return CheckoutConfirmationPageController.process(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return CheckoutThankYouPageController.show(languageTag);
    }

    @Override
    public Call showLogInForm(final String languageTag) {
        return LogInPageController.show(languageTag);
    }

    @Override
    public Call processLogInForm(final String languageTag) {
        return LogInPageController.processLogIn(languageTag);
    }

    @Override
    public Call processSignUpForm(final String languageTag) {
        return LogInPageController.processSignUp(languageTag);
    }

    @Override
    public Call processLogOut(final String languageTag) {
        return LogInPageController.processLogOut(languageTag);
    }

    @Override
    public Call showMyPersonalDetails(final String languageTag) {
        return MyPersonalDetailsPageController.show(languageTag);
    }

    @Override
    public Call processMyPersonalDetailsForm(final String languageTag) {
        return MyPersonalDetailsPageController.process(languageTag);
    }

    @Override
    public Call showMyAddressBook(final String languageTag) {
        return AddressBookController.show(languageTag);
    }

    @Override
    public Call showAddAddressToMyAddressBook(final String languageTag) {
        return AddAddressController.show(languageTag);
    }

    @Override
    public Call processAddAddressToMyAddressBookForm(final String languageTag) {
        return AddAddressController.process(languageTag);
    }

    @Override
    public Call showChangeAddressInMyAddressBook(final String languageTag, final String addressId) {
        return ChangeAddressController.show(languageTag, addressId);
    }

    @Override
    public Call processChangeAddressInMyAddressBookForm(final String languageTag, final String addressId) {
        return ChangeAddressController.process(languageTag, addressId);
    }

    @Override
    public Call processRemoveAddressFromMyAddressBookForm(final String languageTag, final String addressId) {
        return RemoveAddressController.process(languageTag, addressId);
    }

    @Override
    public Call showMyOrders(final String languageTag) {
        return MyOrdersPageController.list(languageTag, 1);
    }

    @Override
    public Call showMyOrder(final String languageTag, final String orderNumber) {
        return MyOrdersPageController.show(languageTag, orderNumber);
    }

    @Override
    public Call processSetupFormCall() {
        return SetupController.processForm();
    }
}
