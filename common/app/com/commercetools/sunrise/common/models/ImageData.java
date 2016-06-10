package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.Image;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class ImageData extends Base {
    private String imageUrl;

    public ImageData() {
    }

    public ImageData(final Image image) {
        this.imageUrl = trimToNull(image.getUrl());
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
