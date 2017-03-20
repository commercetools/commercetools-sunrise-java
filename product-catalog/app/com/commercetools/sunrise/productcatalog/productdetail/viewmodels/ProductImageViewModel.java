package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class ProductImageViewModel extends ViewModel {

    private String thumbImage;
    private String bigImage;

    public ProductImageViewModel() {
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
