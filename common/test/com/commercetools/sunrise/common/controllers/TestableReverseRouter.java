package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.reverserouter.*;
import play.mvc.Call;

public class TestableReverseRouter implements ReverseRouter, HomeReverseRouter, ProductReverseRouter, CheckoutReverseRouter, AddressBookReverseRouter, MyPersonalDetailsReverseRouter, MyOrdersReverseRouter, CartReverseRouter, AuthenticationReverseRouter, LocalizationReverseRouter {

    private String themeAssetsUrl;
    private String changeLanguageUrl;
    private String changeCountryUrl;
    private String showHomeUrl;
    private String showCategoryUrl;
    private String showProductUrl;
    private String processSearchProductsFormUrl;
    private String showCartUrl;
    private String processAddProductToCartFormUrl;
    private String processChangeLineItemQuantityFormUrl;
    private String processDeleteLineItemFormUrl;
    private String showCheckoutAddressesFormUrl;
    private String processCheckoutAddressesFormUrl;
    private String showCheckoutShippingFormUrl;
    private String processCheckoutShippingFormUrl;
    private String showCheckoutPaymentFormUrl;
    private String processCheckoutPaymentFormUrl;
    private String showCheckoutConfirmationFormUrl;
    private String processCheckoutConfirmationFormUrl;
    private String showCheckoutThankYouUrl;
    private String showLogInFormUrl;
    private String processLogInFormUrl;
    private String processSignUpFormUrl;
    private String processLogOutUrl;
    private String showMyPersonalDetailsUrl;
    private String processMyPersonalDetailsFormUrl;
    private String showMyAddressBookUrl;
    private String showAddAddressToAddressBookUrl;
    private String processAddAddressToAddressBookFormUrl;
    private String showChangeAddressInAddressBookUrl;
    private String processChangeAddressInAddressBookFormUrl;
    private String processRemoveAddressFromAddressBookFormUrl;
    private String showMyOrderListUrl;
    private String showMyOrderDetailUrl;

    public void setThemeAssetsUrl(final String themeAssetsUrl) {
        this.themeAssetsUrl = themeAssetsUrl;
    }

    public void setChangeLanguageUrl(final String changeLanguageUrl) {
        this.changeLanguageUrl = changeLanguageUrl;
    }

    public void setChangeCountryUrl(final String changeCountryUrl) {
        this.changeCountryUrl = changeCountryUrl;
    }

    public void setShowHomeUrl(final String showHomeUrl) {
        this.showHomeUrl = showHomeUrl;
    }

    public void setShowCategoryUrl(final String showCategoryUrl) {
        this.showCategoryUrl = showCategoryUrl;
    }

    public void setShowProductUrl(final String showProductUrl) {
        this.showProductUrl = showProductUrl;
    }

    public void setProcessSearchProductsFormUrl(final String processSearchProductsFormUrl) {
        this.processSearchProductsFormUrl = processSearchProductsFormUrl;
    }

    public void setShowCartUrl(final String showCartUrl) {
        this.showCartUrl = showCartUrl;
    }

    public void setProcessAddProductToCartFormUrl(final String processAddProductToCartFormUrl) {
        this.processAddProductToCartFormUrl = processAddProductToCartFormUrl;
    }

    public void setProcessChangeLineItemQuantityFormUrl(final String processChangeLineItemQuantityFormUrl) {
        this.processChangeLineItemQuantityFormUrl = processChangeLineItemQuantityFormUrl;
    }

    public void setProcessDeleteLineItemFormUrl(final String processDeleteLineItemFormUrl) {
        this.processDeleteLineItemFormUrl = processDeleteLineItemFormUrl;
    }

    public void setShowCheckoutAddressesFormUrl(final String showCheckoutAddressesFormUrl) {
        this.showCheckoutAddressesFormUrl = showCheckoutAddressesFormUrl;
    }

    public void setProcessCheckoutAddressesFormUrl(final String processCheckoutAddressesFormUrl) {
        this.processCheckoutAddressesFormUrl = processCheckoutAddressesFormUrl;
    }

    public void setShowCheckoutShippingFormUrl(final String showCheckoutShippingFormUrl) {
        this.showCheckoutShippingFormUrl = showCheckoutShippingFormUrl;
    }

    public void setProcessCheckoutShippingFormUrl(final String processCheckoutShippingFormUrl) {
        this.processCheckoutShippingFormUrl = processCheckoutShippingFormUrl;
    }

    public void setShowCheckoutPaymentFormUrl(final String showCheckoutPaymentFormUrl) {
        this.showCheckoutPaymentFormUrl = showCheckoutPaymentFormUrl;
    }

    public void setProcessCheckoutPaymentFormUrl(final String processCheckoutPaymentFormUrl) {
        this.processCheckoutPaymentFormUrl = processCheckoutPaymentFormUrl;
    }

    public void setShowCheckoutConfirmationFormUrl(final String showCheckoutConfirmationFormUrl) {
        this.showCheckoutConfirmationFormUrl = showCheckoutConfirmationFormUrl;
    }

    public void setProcessCheckoutConfirmationFormUrl(final String processCheckoutConfirmationFormUrl) {
        this.processCheckoutConfirmationFormUrl = processCheckoutConfirmationFormUrl;
    }

    public void setShowCheckoutThankYouUrl(final String showCheckoutThankYouUrl) {
        this.showCheckoutThankYouUrl = showCheckoutThankYouUrl;
    }

    public void setShowLogInFormUrl(final String showLogInFormUrl) {
        this.showLogInFormUrl = showLogInFormUrl;
    }

    public void setProcessLogInFormUrl(final String processLogInFormUrl) {
        this.processLogInFormUrl = processLogInFormUrl;
    }

    public void setProcessSignUpFormUrl(final String processSignUpFormUrl) {
        this.processSignUpFormUrl = processSignUpFormUrl;
    }

    public void setProcessLogOutUrl(final String processLogOutUrl) {
        this.processLogOutUrl = processLogOutUrl;
    }

    public void setShowMyPersonalDetailsUrl(final String showMyPersonalDetailsUrl) {
        this.showMyPersonalDetailsUrl = showMyPersonalDetailsUrl;
    }

    public void setProcessMyPersonalDetailsFormUrl(final String processMyPersonalDetailsFormUrl) {
        this.processMyPersonalDetailsFormUrl = processMyPersonalDetailsFormUrl;
    }

    public void setShowMyAddressBookUrl(final String showMyAddressBookUrl) {
        this.showMyAddressBookUrl = showMyAddressBookUrl;
    }

    public void setShowAddAddressToAddressBookUrl(final String showAddAddressToAddressBookUrl) {
        this.showAddAddressToAddressBookUrl = showAddAddressToAddressBookUrl;
    }

    public void setProcessAddAddressToAddressBookFormUrl(final String processAddAddressToAddressBookFormUrl) {
        this.processAddAddressToAddressBookFormUrl = processAddAddressToAddressBookFormUrl;
    }

    public void setShowChangeAddressInAddressBookUrl(final String showChangeAddressInAddressBookUrl) {
        this.showChangeAddressInAddressBookUrl = showChangeAddressInAddressBookUrl;
    }

    public void setProcessChangeAddressInAddressBookFormUrl(final String processChangeAddressInAddressBookFormUrl) {
        this.processChangeAddressInAddressBookFormUrl = processChangeAddressInAddressBookFormUrl;
    }

    public void setProcessRemoveAddressFromAddressBookFormUrl(final String processRemoveAddressFromAddressBookFormUrl) {
        this.processRemoveAddressFromAddressBookFormUrl = processRemoveAddressFromAddressBookFormUrl;
    }

    public void setShowMyOrderListUrl(final String showMyOrderListUrl) {
        this.showMyOrderListUrl = showMyOrderListUrl;
    }

    public void setShowMyOrderDetailUrl(final String showMyOrderDetailUrl) {
        this.showMyOrderDetailUrl = showMyOrderDetailUrl;
    }

    @Override
    public Call themeAssets(final String file) {
        return createCall(themeAssetsUrl);
    }

    @Override
    public Call processChangeLanguageForm() {
        return createCall(changeLanguageUrl);
    }

    @Override
    public Call processChangeCountryForm(final String languageTag) {
        return createCall(changeCountryUrl);
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return createCall(showHomeUrl);
    }

    @Override
    public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
        return createCall(showCategoryUrl + categorySlug);
    }

    @Override
    public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
        return createCall(showProductUrl + productSlug + sku);
    }

    @Override
    public Call processSearchProductsForm(final String languageTag) {
        return createCall(processSearchProductsFormUrl);
    }

    @Override
    public Call showCart(final String languageTag) {
        return createCall(showCartUrl);
    }

    @Override
    public Call processAddProductToCartForm(final String languageTag) {
        return createCall(processAddProductToCartFormUrl);
    }

    @Override
    public Call processChangeLineItemQuantityForm(final String languageTag) {
        return createCall(processChangeLineItemQuantityFormUrl);
    }

    @Override
    public Call processDeleteLineItemForm(final String languageTag) {
        return createCall(processDeleteLineItemFormUrl);
    }

    @Override
    public Call checkoutAddressesPageCall(final String languageTag) {
        return createCall(showCheckoutAddressesFormUrl);
    }

    @Override
    public Call checkoutAddressesProcessFormCall(final String languageTag) {
        return createCall(processCheckoutAddressesFormUrl);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return createCall(showCheckoutShippingFormUrl);
    }

    @Override
    public Call checkoutShippingProcessFormCall(final String languageTag) {
        return createCall(processCheckoutShippingFormUrl);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return createCall(showCheckoutPaymentFormUrl);
    }

    @Override
    public Call checkoutPaymentProcessFormCall(final String languageTag) {
        return createCall(processCheckoutPaymentFormUrl);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return createCall(showCheckoutConfirmationFormUrl);
    }

    @Override
    public Call checkoutConfirmationProcessFormCall(final String languageTag) {
        return createCall(processCheckoutConfirmationFormUrl);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return createCall(showCheckoutThankYouUrl);
    }

   @Override
    public Call showLogInForm(final String languageTag) {
        return createCall(showLogInFormUrl);
    }

    @Override
    public Call processLogInForm(final String languageTag) {
        return createCall(processLogInFormUrl);
    }

    @Override
    public Call processSignUpForm(final String languageTag) {
        return createCall(processSignUpFormUrl);
    }

    @Override
    public Call processLogOut(final String languageTag) {
        return createCall(processLogOutUrl);
    }

    @Override
    public Call myPersonalDetailsPageCall(final String languageTag) {
        return createCall(showMyPersonalDetailsUrl);
    }

    @Override
    public Call myPersonalDetailsProcessFormCall(final String languageTag) {
        return createCall(processMyPersonalDetailsFormUrl);
    }

    @Override
    public Call addressBookCall(final String languageTag) {
        return createCall(showMyAddressBookUrl);
    }

    @Override
    public Call addAddressToAddressBookCall(final String languageTag) {
        return createCall(showAddAddressToAddressBookUrl);
    }

    @Override
    public Call addAddressToAddressBookProcessFormCall(final String languageTag) {
        return createCall(processAddAddressToAddressBookFormUrl);
    }

    @Override
    public Call changeAddressInAddressBookCall(final String languageTag, final String addressId) {
        return createCall(showChangeAddressInAddressBookUrl);
    }

    @Override
    public Call changeAddressInAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return createCall(processChangeAddressInAddressBookFormUrl);
    }

    @Override
    public Call removeAddressFromAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return createCall(processRemoveAddressFromAddressBookFormUrl);
    }

    @Override
    public Call myOrderListPageCall(final String languageTag) {
        return createCall(showMyOrderListUrl);
    }

    @Override
    public Call myOrderDetailPageCall(final String languageTag, final String orderNumber) {
        return createCall(showMyOrderDetailUrl);
    }

    private Call createCall(final String url) {
        if (url == null) {
            throw new UnsupportedOperationException();
        }
        return new Call() {
            @Override
            public String url() {
                return url;
            }

            @Override
            public String method() {
                return null;
            }

            @Override
            public String fragment() {
                return null;
            }
        };
    }
}
