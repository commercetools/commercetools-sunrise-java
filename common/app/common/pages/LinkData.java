package common.pages;

public class LinkData {
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
