package productcatalog.productdetail;

import common.controllers.PageContent;
import productcatalog.common.BreadcrumbData;
import productcatalog.common.ProductBean;
import productcatalog.common.SuggestionsData;

import java.util.List;

public class ProductDetailPageContent extends PageContent {
    private BreadcrumbData breadcrumb;
    private ProductBean product;
    private List<ShippingRateData> shippingRates;
    private SuggestionsData suggestions;
    private String addToCartFormUrl;

    public ProductDetailPageContent() {
    }

    public BreadcrumbData getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(final BreadcrumbData breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(final ProductBean product) {
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