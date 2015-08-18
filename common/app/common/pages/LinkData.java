package common.pages;

import io.sphere.sdk.models.Base;

public class LinkData extends Base {
    private final String text;
    private final String url;

    public LinkData(final String text, final String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}
