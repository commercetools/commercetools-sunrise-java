package productcatalog.pages;

import common.pages.CollectionData;
import common.pages.DetailData;

public class ProductData {

    private final String text;
    private final String sku;
    private final CollectionData ratingList;
    private final String description;
    private final String viewDetailsText;
    private final String price;
    private final String priceOld;
    private final CollectionData colorList;
    private final CollectionData sizeList;
    private final String sizeGuideText;
    private final CollectionData bagItemList;
    private final String addToBagText;
    private final String addToWishlistText;
    private final String availableText;
    private final DetailData productDetails;
    private final DetailData deliveryAndReturn;


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
        this.deliveryAndReturn = builder.deliveryAndReturn;
    }

    public String getText() {
        return text;
    }

    public String getSku() {
        return sku;
    }

    public CollectionData getRating() {
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

    public CollectionData getColor() {
        return colorList;
    }

    public CollectionData getSize() {
        return sizeList;
    }

    public String getSizeGuide() {
        return sizeGuideText;
    }

    public CollectionData getBagItems() {
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

    public DetailData getProductDetails() {
        return productDetails;
    }

    public DetailData getDeliveryAndReturn() {
        return deliveryAndReturn;
    }
}
