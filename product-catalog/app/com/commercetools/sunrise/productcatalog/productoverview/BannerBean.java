package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.models.TitleDescriptionBean;

public class BannerBean extends TitleDescriptionBean {

    private String imageMobile;
    private String imageDesktop;

    public BannerBean() {
    }

    public String getImageMobile() {
        return imageMobile;
    }

    public void setImageMobile(final String imageMobile) {
        this.imageMobile = imageMobile;
    }

    public String getImageDesktop() {
        return imageDesktop;
    }

    public void setImageDesktop(final String imageDesktop) {
        this.imageDesktop = imageDesktop;
    }
}
