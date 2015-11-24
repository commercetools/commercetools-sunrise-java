package productcatalog.controllers;

import common.controllers.PageContent;
import productcatalog.models.BreadcrumbData;
import productcatalog.models.ProductData;
import productcatalog.models.ProductThumbnailData;
import productcatalog.models.ShippingRateData;

import java.util.List;

public class ProductDetailPageContent extends PageContent {
    private String additionalTitle;
    private BreadcrumbData breadcrumb;
    private ProductData productData;
    private List<ShippingRateData> shippingRates;
    private List<ProductThumbnailData> suggestions;
    private String addToCartFormUrl;

    public ProductDetailPageContent() {
    }

    public ProductDetailPageContent(final String additionalTitle) {
        this.additionalTitle = additionalTitle;
        this.productData = productData;
    }

    @Override
    public String getAdditionalTitle() {
        return additionalTitle;
    }

    public BreadcrumbData getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbData breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(final ProductData productData) {
        this.productData = productData;
    }

    public List<ShippingRateData> getShippingRates() {
        return shippingRates;
    }

    public void setShippingRates(final List<ShippingRateData> shippingRates) {
        this.shippingRates = shippingRates;
    }

    public List<ProductThumbnailData> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final List<ProductThumbnailData> suggestions) {
        this.suggestions = suggestions;
    }

    public String getAddToCartFormUrl() {
        return addToCartFormUrl;
    }

    public void setAddToCartFormUrl(final String addToCartFormUrl) {
        this.addToCartFormUrl = addToCartFormUrl;
    }
}