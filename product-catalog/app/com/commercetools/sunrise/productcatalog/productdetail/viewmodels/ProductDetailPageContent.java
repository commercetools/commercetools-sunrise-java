package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs.BreadcrumbViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListViewModel;

public class ProductDetailPageContent extends PageContent {

    private BreadcrumbViewModel breadcrumb;
    private ProductViewModel product;
    private ProductListViewModel suggestions;

    public ProductDetailPageContent() {
    }

    public BreadcrumbViewModel getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbViewModel breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductViewModel getProduct() {
        return product;
    }

    public void setProduct(final ProductViewModel product) {
        this.product = product;
    }

    public ProductListViewModel getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final ProductListViewModel suggestions) {
        this.suggestions = suggestions;
    }
}