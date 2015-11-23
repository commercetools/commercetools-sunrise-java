package common.controllers;

import play.mvc.Call;

public class TestableReverseRouter implements ReverseRouter {
    private String homeUrl;
    private String categoryUrl;
    private String searchUrl;
    private String productUrl;
    private String addToCartFormUrl;
    private String showCheckoutShippingFormUrl;
    private String processCheckoutShippingFormUrl;
    private String showCheckoutPaymentFormUrl;
    private String processCheckoutPaymentFormUrl;
    private String showCheckoutConfirmationFormUrl;

    public void setHomeUrl(final String homeUrl) {
        this.homeUrl = homeUrl;
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

    @Override
    public Call home(final String languageTag) {
        return createCall(homeUrl);
    }

    @Override
    public Call category(final String languageTag, final String categorySlug, final int page) {
        return createCall(categoryUrl + categorySlug);
    }

    @Override
    public Call category(final String languageTag, final String categorySlug) {
        return createCall(categoryUrl + categorySlug);
    }

    @Override
    public Call search(final String languageTag, final String searchTerm, final int page) {
        return createCall(searchUrl + searchTerm);
    }

    @Override
    public Call search(final String languageTag, final String searchTerm) {
        return createCall(searchUrl + searchTerm);
    }

    @Override
    public Call product(final String locale, final String productSlug, final String sku) {
        return createCall(productUrl + productSlug + sku);
    }

    @Override
    public Call productVariantToCartForm(final String languageTag) {
        return createCall(addToCartFormUrl);
    }

    @Override
    public Call showCheckoutShippingForm(final String language) {
        return createCall(showCheckoutShippingFormUrl);
    }

    @Override
    public Call processCheckoutShippingForm(final String language) {
        return createCall(processCheckoutShippingFormUrl);
    }

    @Override
    public Call showCheckoutPaymentForm(final String language) {
        return createCall(showCheckoutPaymentFormUrl);
    }

    @Override
    public Call processCheckoutPaymentForm(final String language) {
        return createCall(processCheckoutPaymentFormUrl);
    }

    @Override
    public Call showCheckoutConfirmationForm(final String language) {
        return createCall(showCheckoutConfirmationFormUrl);
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
