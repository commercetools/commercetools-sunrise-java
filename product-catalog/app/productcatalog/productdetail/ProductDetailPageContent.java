package productcatalog.productdetail;

import common.controllers.PageContent;
import productcatalog.common.BreadcrumbBean;
import productcatalog.common.ProductBean;
import productcatalog.common.SuggestionsData;

import java.util.List;

public class ProductDetailPageContent extends PageContent {
    private BreadcrumbBean breadcrumb;
    private ProductBean product;
    private List<ShippingRateBean> shippingRates;
    private SuggestionsData suggestions;
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