package productcatalog.pages;

import common.models.DetailData;

public class BannerData extends DetailData {
    private String imageMobile;
    private String imageDesktop;

    public BannerData() {
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
