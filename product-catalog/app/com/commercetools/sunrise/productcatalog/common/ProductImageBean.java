package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ViewModel;

public class ProductImageBean extends ViewModel {

    private String thumbImage;
    private String bigImage;

    public ProductImageBean() {
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
