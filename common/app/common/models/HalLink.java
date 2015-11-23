package common.models;

public class HalLink {
    private String href;

    public HalLink() {
    }

    public HalLink(final String href) {
        setHref(href);
    }

    public String getHref() {
        return href;
    }

    public void setHref(final String href) {
        this.href = href;
    }
}
