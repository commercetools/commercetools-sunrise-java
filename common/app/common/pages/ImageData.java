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
        return nullIfEmpty(image.getUrl());
    }

    public String getBigImage() {
        return nullIfEmpty(image.getUrl());
    }

    public String getPlaceholderImage() {
        return "http://placehold.it/350x150";
    }

    private String nullIfEmpty(final String s) {
        if(s.isEmpty())
            return null;
        else
            return s;
    }
}
