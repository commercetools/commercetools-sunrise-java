package common.controllers;

import play.mvc.Call;

public class TestableReverseRouter implements ReverseRouter {
    private String homeUrl;
    private String changeLanguageUrl;
    private String changeCountryUrl;
    private String categoryUrl;
    private String searchUrl;
    private String productUrl;
    private String addToCartFormUrl;
    private String showCheckoutAddressesFormUrl;
    private String processCheckoutAddressesFormUrl;
    private String showCheckoutShippingFormUrl;
    private String processCheckoutShippingFormUrl;
    private String showCheckoutPaymentFormUrl;
    private String processCheckoutPaymentFormUrl;
    private String showCheckoutConfirmationFormUrl;
    private String designAssets;
    private String processCheckoutConfirmationForm;
    private String showCart;
    private String processDeleteLineItem;
    private String processChangeLineItemQuantity;
    private String showCheckoutThankyou;

    public void setHomeUrl(final String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public void setChangeLanguageUrl(final String changeLanguageUrl) {
        this.changeLanguageUrl = changeLanguageUrl;
    }

    public void setChangeCountryUrl(final String changeCountryUrl) {
        this.changeCountryUrl = changeCountryUrl;
    }

    public void setCategoryUrl(final String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public void setSearchUrl(final String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public void setProductUrl(final String productUrl) {
        this.productUrl = productUrl;
    }

    public void setAddToCartFormUrl(final String addToCartFormUrl) {
        this.addToCartFormUrl = addToCartFormUrl;
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

    public void setDesignAssets(final String designAssets) {
        this.designAssets = designAssets;
    }

    public void setProcessCheckoutConfirmationForm(final String processCheckoutConfirmationForm) {
        this.processCheckoutConfirmationForm = processCheckoutConfirmationForm;
    }

    public void setShowCart(final String showCart) {
        this.showCart = showCart;
    }

    public void setProcessDeleteLineItem(final String processDeleteLineItem) {
        this.processDeleteLineItem = processDeleteLineItem;
    }

    public void setProcessChangeLineItemQuantity(final String processChangeLineItemQuantity) {
        this.processChangeLineItemQuantity = processChangeLineItemQuantity;
    }

    public void setShowCheckoutThankyou(final String showCheckoutThankyou) {
        this.showCheckoutThankyou = showCheckoutThankyou;
    }

    @Override
    public Call home(final String languageTag) {
        return createCall(homeUrl);
    }

    @Override
    public Call changeLanguage() {
        return createCall(changeLanguageUrl);
    }

    @Override
    public Call changeCountry(final String languageTag) {
        return createCall(changeCountryUrl);
    }

    @Override
    public Call category(final String languageTag, final String categorySlug) {
        return createCall(categoryUrl + categorySlug);
    }

    @Override
    public Call search(final String languageTag) {
        return createCall(searchUrl);
    }

    @Override
    public Call product(final String languageTag, final String productSlug, final String sku) {
        return createCall(productUrl + productSlug + sku);
    }

    @Override
    public Call productToCartForm(final String languageTag) {
        return createCall(addToCartFormUrl);
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
    public Call designAssets(final String file) {
        return createCall(designAssets);
    }

    @Override
    public Call processCheckoutConfirmationForm(final String languageTag) {
        return createCall(processCheckoutConfirmationForm);
    }

    @Override
    public Call showCart(final String languageTag) {
        return createCall(showCart);
    }

    @Override
    public Call processDeleteLineItem(final String languageTag) {
        return createCall(processDeleteLineItem);
    }

    @Override
    public Call processChangeLineItemQuantity(final String languageTag) {
        return createCall(processChangeLineItemQuantity);
    }

    @Override
    public Call showCheckoutThankyou(final String languageTag) {
        return createCall(showCheckoutThankyou);
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
