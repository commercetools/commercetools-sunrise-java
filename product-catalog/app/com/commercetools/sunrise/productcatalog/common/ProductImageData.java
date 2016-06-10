package com.commercetools.sunrise.productcatalog.common;

import io.sphere.sdk.products.Image;

public class ProductImageData {
    private String thumbImage;
    private String bigImage;

    public ProductImageData() {
    }

    public ProductImageData(final Image image) {
        this.thumbImage = image.getUrl();
        this.bigImage = image.getUrl();
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(final String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(final String bigImage) {
        this.bigImage = bigImage;
    }
}
