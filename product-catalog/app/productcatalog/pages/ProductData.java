package productcatalog.pages;

import common.pages.DetailData;
import common.pages.SelectableData;
import io.sphere.sdk.models.Base;

import java.util.List;

public class ProductData extends Base {

    private final String text;
    private final String sku;
    private final String description;
    private final String price;
    private final String priceOld;
    private final List<SelectableData> colorList;
    private final List<SelectableData> sizeList;
    private final List<DetailData> productDetails;

    public ProductData (final String text, final String sku, final String description, final String price, final String priceOld, final List<SelectableData> colorList, final List<SelectableData> sizeList, final List<DetailData> productDetails) {
        this.text = text;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.priceOld = priceOld;
        this.colorList = colorList;
        this.sizeList = sizeList;
        this.productDetails = productDetails;
    }

    public String getText() {
        return text;
    }

    public String getSku() {
        return sku;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceOld() {
        return priceOld;
    }

    public List<SelectableData> getColor() {
        return colorList;
    }

    public List<SelectableData> getSize() {
        return sizeList;
    }

    public List<DetailData> getProductDetails() {
        return productDetails;
    }
}
