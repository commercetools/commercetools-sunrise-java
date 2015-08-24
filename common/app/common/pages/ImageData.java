package common.pages;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.Image;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class ImageData extends Base {

    private final String imageUrl;

    private ImageData(final Image image) {
        this.imageUrl = trimToNull(image.getUrl());
    }

    public static ImageData of(final Image image) {
        return new ImageData(image);
    }

    public String getBigImage() {
        return imageUrl;
    }

    public String getThumbImage() {
        return imageUrl;
    }
}
