package common.models;

import io.sphere.sdk.models.Base;

public class ProductThumbnailData extends Base {

    private final String text;
    private final String description;
    private final String imageUrl;
    private final String price;

    public ProductThumbnailData(final String text, final String description, final String imageUrl, final String price) {
        this.text = text;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }
}
