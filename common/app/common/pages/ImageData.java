package common.pages;

import io.sphere.sdk.models.Image;

public class ImageData {

    private final Image image;

    private ImageData(final Image image) {
        this.image = image;
    }

    public static ImageData of(final Image image) {
        return new ImageData(image);
    }

    public String getThumbImage() {
        return image.getUrl();
    }

    public String getBigImage() {
        return image.getUrl();
    }
}
