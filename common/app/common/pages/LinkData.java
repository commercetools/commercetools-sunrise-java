package common.pages;

public class LinkData {
    private final String name;
    private final String url;

    public LinkData(final String name, final String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
