package common.pages;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Image;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class ImageData extends Base {

    private final Image image;

    private ImageData(final Image image) {
        this.image = image;
    }

    public static ImageData of(final Image image) {
        return new ImageData(image);
    }

    public String getThumbImage() {
        return trimToNull(image.getUrl());
    }

    public String getBigImage() {
        return trimToNull(image.getUrl());
    }

    public String getPlaceholderImage() {
        return "http://placehold.it/350x150";
    }
}
