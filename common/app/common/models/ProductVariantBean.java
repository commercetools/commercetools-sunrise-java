package common.models;

import io.sphere.sdk.carts.LineItem;

public class ProductVariantBean {
    private String name;
    private String description;
    private String image;
    private String sku;
    private long quantity;
    private String priceOld;
    private String price;
    private String total;

    public ProductVariantBean() {
    }

    public ProductVariantBean(final LineItem lineItem) {

    }
}
