package com.commercetools.sunrise.common.controllers;

import play.mvc.Call;

public class TestableReverseRouter implements ReverseRouter {
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
    private String showMyPersonalDetailsFormUrl;
    private String processMyPersonalDetailsFormUrl;
    private String showMyOrdersUrl;
    private String showMyOrderUrl;

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

    public void setShowMyPersonalDetailsFormUrl(final String showMyPersonalDetailsFormUrl) {
        this.showMyPersonalDetailsFormUrl = showMyPersonalDetailsFormUrl;
    }

    public void setProcessMyPersonalDetailsFormUrl(final String processMyPersonalDetailsFormUrl) {
        this.processMyPersonalDetailsFormUrl = processMyPersonalDetailsFormUrl;
    }

    public void setShowMyOrdersUrl(final String showMyOrdersUrl) {
        this.showMyOrdersUrl = showMyOrdersUrl;
    }

    public void setShowMyOrderUrl(final String showMyOrderUrl) {
        this.showMyOrderUrl = showMyOrderUrl;
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
    public Call showCheckoutAddressesForm(final String languageTag) {
        return createCall(showCheckoutAddressesFormUrl);
    }

    @Override
    public Call processCheckoutAddressesForm(final String languageTag) {
        return createCall(processCheckoutAddressesFormUrl);
    }

    @Override
    public Call showCheckoutShippingForm(final String languageTag) {
        return createCall(showCheckoutShippingFormUrl);
    }

    @Override
    public Call processCheckoutShippingForm(final String languageTag) {
        return createCall(processCheckoutShippingFormUrl);
    }

    @Override
    public Call showCheckoutPaymentForm(final String languageTag) {
        return createCall(showCheckoutPaymentFormUrl);
    }

    @Override
    public Call processCheckoutPaymentForm(final String languageTag) {
        return createCall(processCheckoutPaymentFormUrl);
    }

    @Override
    public Call showCheckoutConfirmationForm(final String languageTag) {
        return createCall(showCheckoutConfirmationFormUrl);
    }

    @Override
    public Call processCheckoutConfirmationForm(final String languageTag) {
        return createCall(processCheckoutConfirmationFormUrl);
    }

    @Override
    public Call showCheckoutThankYou(final String languageTag) {
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
    public Call showMyPersonalDetailsForm(final String languageTag) {
        return createCall(showMyPersonalDetailsFormUrl);
    }

    @Override
    public Call processMyPersonalDetailsForm(final String languageTag) {
        return createCall(processMyPersonalDetailsFormUrl);
    }

    @Override
    public Call showMyOrders(final String languageTag) {
        return createCall(showMyOrdersUrl);
    }

    @Override
    public Call showMyOrder(final String languageTag, final String orderNumber) {
        return createCall(showMyOrderUrl);
    }

    private Call createCall(final String url) {
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
