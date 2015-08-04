package productcatalog.pages;

import common.pages.CollectionData;
import common.pages.DetailData;
import common.pages.SelectableData;

import javax.swing.event.ListDataEvent;

public class ProductData {
    private static final String SKU_PREFIX = "SUNRISE #";

    private final String text;
    private final String sku;
    private final CollectionData<SelectableData> ratingList;
    private final String description;
    private final String viewDetailsText;
    private final String price;
    private final String priceOld;
    private final CollectionData<SelectableData> colorList;
    private final CollectionData<SelectableData> sizeList;
    private final String sizeGuideText;
    private final CollectionData<SelectableData> bagItemList;
    private final String addToBagText;
    private final String addToWishlistText;
    private final String availableText;
    private final CollectionData<DetailData> productDetails;
    private final String deliveryAndReturns;
    private final CollectionData<DetailData> delivery;
    private final CollectionData<DetailData> returns;

    ProductData (ProductDataBuilder builder) {
        this.text = builder.text;
        this.sku = builder.sku;
        this.ratingList = builder.ratingList;
        this.description = builder.description;
        this.viewDetailsText = builder.viewDetailsText;
        this.price = builder.price;
        this.priceOld = builder.priceOld;
        this.colorList = builder.colorList;
        this.sizeList = builder.sizeList;
        this.sizeGuideText = builder.sizeGuideText;
        this.bagItemList = builder.bagItemList;
        this.addToBagText = builder.addToBagText;
        this.addToWishlistText = builder.addToWishlistText;
        this.availableText = builder.availableText;
        this.productDetails = builder.productDetails;
        this.deliveryAndReturns = builder.deliveryAndReturns;
        this.delivery = builder.delivery;
        this.returns = builder.returns;
    }

    public String getText() {
        return text;
    }

    public String getSku() {
        return SKU_PREFIX + sku;
    }

    public CollectionData<SelectableData> getRating() {
        return ratingList;
    }

    public String getDescription() {
        return description;
    }

    public String getViewDetails() {
        return viewDetailsText;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceOld() {
        return priceOld;
    }

    public CollectionData<SelectableData> getColor() {
        return colorList;
    }

    public CollectionData<SelectableData> getSize() {
        return sizeList;
    }

    public String getSizeGuide() {
        return sizeGuideText;
    }

    public CollectionData<SelectableData> getBagItems() {
        return bagItemList;
    }

    public String getAddToBag() {
        return addToBagText;
    }

    public String getAddToWishlist() {
        return addToWishlistText;
    }

    public String getAvailable() {
        return availableText;
    }

    public CollectionData<DetailData> getProductDetails() {
        return productDetails;
    }

    public String getDeliveryAndReturns() {
        return deliveryAndReturns;
    }

    public CollectionData<DetailData> getDelivery() {
        return delivery;
    }

    public CollectionData<DetailData> getReturns() {
        return returns;
    }
}
