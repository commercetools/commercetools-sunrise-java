package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import com.commercetools.sunrise.productcatalog.common.ProductBean;
import com.commercetools.sunrise.productcatalog.common.SuggestionsBean;

import java.util.List;

public class ProductDetailPageContent extends PageContent {

    private BreadcrumbBean breadcrumb;
    private ProductBean product;
    private List<ShippingRateBean> shippingRates;
    private SuggestionsBean suggestions;
    private String addToCartFormUrl;

    public ProductDetailPageContent() {
    }

    public BreadcrumbBean getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbBean breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(final ProductBean product) {
        this.product = product;
    }

    public List<ShippingRateBean> getShippingRates() {
        return shippingRates;
    }

    public void setShippingRates(final List<ShippingRateBean> shippingRates) {
        this.shippingRates = shippingRates;
    }

    public SuggestionsBean getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final SuggestionsBean suggestions) {
        this.suggestions = suggestions;
    }

    public String getAddToCartFormUrl() {
        return addToCartFormUrl;
    }

    public void setAddToCartFormUrl(final String addToCartFormUrl) {
        this.addToCartFormUrl = addToCartFormUrl;
    }
}