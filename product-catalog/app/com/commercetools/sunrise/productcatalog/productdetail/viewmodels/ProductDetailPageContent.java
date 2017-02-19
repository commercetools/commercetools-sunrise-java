package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.BreadcrumbBean;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListBean;

public class ProductDetailPageContent extends PageContent {

    private BreadcrumbBean breadcrumb;
    private ProductBean product;
    private ProductListBean suggestions;

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

    public ProductListBean getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final ProductListBean suggestions) {
        this.suggestions = suggestions;
    }
}