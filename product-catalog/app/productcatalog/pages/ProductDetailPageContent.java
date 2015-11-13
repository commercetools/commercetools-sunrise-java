package productcatalog.pages;

import common.pages.*;

import java.util.List;

public class ProductDetailPageContent extends PageContent {
    private final String additionalTitle;
    private final PdpStaticData staticData;
    private final List<SelectableLinkData> breadcrumb;
    private final ProductData productData;
    private final List<ShippingRateData> shippingRates;
    private final List<ProductData> suggestions;
    private final String addToCartFormUrl;

    public ProductDetailPageContent(final String additionalTitle, final PdpStaticData staticData, final List<SelectableLinkData> breadcrumb,
                                    final ProductData productData, final List<ShippingRateData> shippingRates,
                                    final List<ProductData> suggestions, final String addToCartFormUrl) {
        this.additionalTitle = additionalTitle;
        this.staticData = staticData;
        this.breadcrumb = breadcrumb;
        this.productData = productData;
        this.shippingRates = shippingRates;
        this.suggestions = suggestions;
        this.addToCartFormUrl = addToCartFormUrl;
    }

    public PdpStaticData getStatic() {
        return staticData;
    }

    public String additionalTitle() {
        return additionalTitle;
    }

    public List<SelectableLinkData> getBreadcrumb() {
        return breadcrumb;
    }

    public ProductData getProduct() {
        return productData;
    }

    public List<ProductData> getSuggestions() {
        return suggestions;
    }

    public List<ShippingRateData> getShippingRates() {
        return shippingRates;
    }

    public String getAddToCartFormUrl() {
        return addToCartFormUrl;
    }
}