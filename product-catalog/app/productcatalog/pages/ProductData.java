package productcatalog.pages;

import common.contexts.AppContext;
import common.pages.CollectionData;
import common.pages.DetailData;
import common.pages.SelectableData;
import io.sphere.sdk.products.ProductProjection;

import java.util.Arrays;

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

    public CollectionData getRating() {
        return new CollectionData("4/5", Arrays.asList(
                new SelectableData("5 Stars", "5", "", "", false),
                new SelectableData("4 Stars", "4", "", "", false),
                new SelectableData("3 Stars", "3", "", "", false),
                new SelectableData("2 Stars", "2", "", "", false),
                new SelectableData("1 Stars", "1", "", "", false)
        ));
    }

    public String getDescription() {
        return product.getDescription().map(description -> withSuitableLanguage(description, context))
                .orElse("");
    }

    public String getViewDetailsText() {
        return "View Details";
    }

    public String getPrice() {
        return "13.50";
    }

    public String getPriceOld() {
        return "15.00";
    }

    public CollectionData getColor() {
        return new CollectionData("Color", Arrays.asList(
                new SelectableData("Choose Color", "none", "", "", true),
                new SelectableData("Blue", "blue", "", "", false),
                new SelectableData("Red", "red", "", "", false)
        ));
    }

    public CollectionData getSize() {
        return new CollectionData("Size", Arrays.asList(
                new SelectableData("Choose Size", "none", "", "", true),
                new SelectableData("l", "l", "", "", false),
                new SelectableData("m", "m", "", "", false),
                new SelectableData("s", "s", "", "", false)
        ));
    }

    public String getSizeGuide() {
        return "Size Guide";
    }

    public CollectionData getBagItems() {
        return new CollectionData("", Arrays.asList(
                new SelectableData("1", "", "", "", true),
                new SelectableData("2", "", "", "", false),
                new SelectableData("3", "", "", "", false)
        ));
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

    public DetailData getProductDetails() {
        return new DetailData("Product Details", "detailed description");
    }

    public DetailData getDeliveryAndReturn() {
        return new DetailData("Delivery & Returns", "delivery description");
    }
}
