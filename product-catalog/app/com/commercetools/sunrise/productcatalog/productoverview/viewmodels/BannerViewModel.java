package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.TitleDescriptionViewModel;

public class BannerViewModel extends TitleDescriptionViewModel {

    private String imageMobile;
    private String imageDesktop;

    public BannerViewModel() {
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
