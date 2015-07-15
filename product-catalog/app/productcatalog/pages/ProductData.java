package productcatalog.pages;

import common.contexts.AppContext;
import io.sphere.sdk.products.ProductProjection;

import static common.utils.Languages.withSuitableLanguage;

public class ProductData {

    private final ProductProjection product;
    private final AppContext context;

    public ProductData(final ProductProjection product, final AppContext context) {
        this.product = product;
        this.context = context;
    }

    public String getText() {
        return withSuitableLanguage(product.getName(), context);
    }

    public String getSku() {
        return product.getMasterVariant().getSku().orElse("");
    }

    public RatingListData getRating() {
        return new RatingListData();
    }

    public String getDescription() {
        return product.getDescription().map(description -> withSuitableLanguage(description, context))
                .orElse("");
    }

    public String getViewDetails() {
        return "View Details";
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
        return "Size Guide";
    }

    public BagItemListData getBagItems() {
        return new BagItemListData();
    }

    public String getAddToBag() {
        return "Add to Bag";
    }

    public String getAddToWishlist() {
        return "Add to Wishlist";
    }

    public String getAvailable() {
        return "Available";
    }

    public ProductDetailData getProductDetails() {
        return new ProductDetailData();
    }

    public DeliveryAndReturnData getDeliveryAndReturn() {
        return new DeliveryAndReturnData();
    }
}
