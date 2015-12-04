package productcatalog.controllers;

import common.controllers.PageContent;
import productcatalog.models.*;

import java.util.List;

public class ProductDetailPageContent extends PageContent {
    private String additionalTitle;
    private BreadcrumbData breadcrumb;
    private ProductData product;
    private List<ShippingRateData> shippingRates;
    private SuggestionsData suggestions;
    private String addToCartFormUrl;

    public ProductDetailPageContent() {
    }

    public ProductDetailPageContent(final String additionalTitle) {
        this.additionalTitle = additionalTitle;
    }

    @Override
    public String getAdditionalTitle() {
        return additionalTitle;
    }

    public void setAdditionalTitle(final String additionalTitle) {
        this.additionalTitle = additionalTitle;
    }

    public BreadcrumbData getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbData breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductData getProduct() {
        return product;
    }

    public void setProduct(final ProductData product) {
        this.product = product;
    }

    public List<ShippingRateData> getShippingRates() {
        return shippingRates;
    }

    public void setShippingRates(final List<ShippingRateData> shippingRates) {
        this.shippingRates = shippingRates;
    }

    public SuggestionsData getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final SuggestionsData suggestions) {
        this.suggestions = suggestions;
    }

    public String getAddToCartFormUrl() {
        return addToCartFormUrl;
    }

    public void setAddToCartFormUrl(final String addToCartFormUrl) {
        this.addToCartFormUrl = addToCartFormUrl;
    }
}