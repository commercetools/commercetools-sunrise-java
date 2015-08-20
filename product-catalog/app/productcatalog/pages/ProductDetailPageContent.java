package productcatalog.pages;

import common.pages.*;

import java.util.List;

public class ProductDetailPageContent extends PageContent {

    private final String additionalTitle;
    private final PdpStaticData staticData;
    private final List<LinkData> breadcrumbs;
    private final List<ImageData> gallery;
    private final ProductData productData;
    private final List<ShippingRateData> deliveryData;
    private final List<ProductThumbnailData> suggestions;

    public ProductDetailPageContent(final String additionalTitle, final PdpStaticData staticData, final List<LinkData> breadcrumbs, final List<ImageData> gallery, final ProductData productData, final List<ShippingRateData> deliveryData, final List<ProductThumbnailData> suggestions) {
        this.additionalTitle = additionalTitle;
        this.staticData = staticData;
        this.breadcrumbs = breadcrumbs;
        this.gallery = gallery;
        this.productData = productData;
        this.deliveryData = deliveryData;
        this.suggestions = suggestions;
    }

    public PdpStaticData getStatic() {
        return staticData;
    }

    public String additionalTitle() {
        return additionalTitle;
    }

    public List<LinkData> getBreadcrumb() {
        return breadcrumbs;
    }

    public List<ImageData> getGallery() {
        return gallery;
    }

    public ProductData getProduct() {
        return productData;
    }

    public List<ProductThumbnailData> getSuggestions() {
        return suggestions;
    }

    public List<ShippingRateData> getDelivery() {
        return deliveryData;
    }
}