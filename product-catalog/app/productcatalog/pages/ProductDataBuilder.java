package productcatalog.pages;

import common.pages.CollectionData;
import common.pages.DetailData;

import java.util.Collections;

public class ProductDataBuilder {

    String text = "";
    String sku = "";
    CollectionData ratingList = new CollectionData("", Collections.emptyList());
    String description = "";
    String additionalDescription = "";
    String viewDetailsText = "";
    String price = "";
    String priceOld = "";
    CollectionData colorList = new CollectionData("", Collections.emptyList());
    CollectionData sizeList = new CollectionData("", Collections.emptyList());
    String sizeGuideText = "";
    CollectionData bagItemList = new CollectionData("", Collections.emptyList());
    String addToBagText = "";
    String addToWishlistText = "";
    String availableText = "";
    DetailData productDetails = new DetailData("", "");
    DetailData deliveryAndReturn = new DetailData("", "");

    private ProductDataBuilder() {

    }

    public static ProductDataBuilder of() {
        return new ProductDataBuilder();
    }

    public ProductDataBuilder withText(final String text) {
        this.text = text;
        return this;
    }

    public ProductDataBuilder withSku(final String sku) {
        this.sku = sku;
        return this;
    }

    public ProductDataBuilder withRatingList(final CollectionData ratingList) {
        this.ratingList = ratingList;
        return this;
    }

    public ProductDataBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public ProductDataBuilder withAdditionalDescription(final String additionalDescription) {
        this.additionalDescription = additionalDescription;
        return this;
    }

    public ProductDataBuilder withViewDetailsText(final String viewDetailsText) {
        this.viewDetailsText = viewDetailsText;
        return this;
    }

    public ProductDataBuilder withPrice(final String price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder withPriceOld(final String priceOld) {
        this.priceOld = priceOld;
        return this;
    }

    public ProductDataBuilder withColorList(final CollectionData colorList) {
        this.colorList = colorList;
        return this;
    }

    public ProductDataBuilder withSizeList(final CollectionData sizeList) {
        this.sizeList = sizeList;
        return this;
    }

    public ProductDataBuilder withSizeGuideText(final String sizeGuideText) {
        this.sizeGuideText = sizeGuideText;
        return this;
    }

    public ProductDataBuilder withBagItemList(final CollectionData bagItemList) {
        this.bagItemList = bagItemList;
        return this;
    }

    public ProductDataBuilder withAddToBagText(final String addToBagText) {
        this.addToBagText = addToBagText;
        return this;
    }

    public ProductDataBuilder withAddToWishlistText(final String addToWishlistText) {
        this.addToWishlistText = addToWishlistText;
        return this;
    }

    public ProductDataBuilder withAvailableText(final String availableText) {
        this.availableText = availableText;
        return this;
    }

    public ProductDataBuilder withProductDetails(final DetailData productDetails) {
        this.productDetails = productDetails;
        return this;
    }

    public ProductDataBuilder withDeliveryAndReturn(final DetailData deliveryAndReturn) {
        this.deliveryAndReturn = deliveryAndReturn;
        return this;
    }

    public ProductData build() {
        return new ProductData(this);
    }
}