package productcatalog.pages;

import io.sphere.sdk.products.ProductProjection;

public class ProductData {

    private final ProductProjection product;

    public ProductData(final ProductProjection product) {
        this.product = product;
    }

    public String getText() {
        return "getText";
    }

    public String getSku() {
        return "getSku";
    }

    public RatingListData getRating() {
        return new RatingListData();
    }

    public String getdescription() {
        return "description";
    }

    public String getViewDetails() {
        return "viewDetails";
    }

    public String getPrice() {
        return "13.50";
    }

    public String getPriceOld() {
        return "15.00";
    }

    public ColorListData getColor() {
        return new ColorListData();
    }

    public SizeListData getSize() {
        return new SizeListData();
    }

    public String getSizeGuide() {
        return "SizeGuide";
    }

    public BagItemListData getBagItems() {
        return new BagItemListData();
    }

    public String getAddToBag() {
        return "addToBag";
    }

    public String getAddToWishlist() {
        return "addToWishlist";
    }

    public String getAvailable() {
        return "10 Stk";
    }

    public ProductDetailData getProductDetails() {
        return new ProductDetailData();
    }

    public DeliveryAndReturnData getDeliveryAndReturn() {
        return new DeliveryAndReturnData();
    }
}
