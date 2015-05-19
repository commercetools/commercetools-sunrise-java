package models;

public class Breadcrumb {
    public String url;
    public String name;
    public boolean isCurrent;

    public Breadcrumb(final String url, final String name, final boolean isCurrent) {
        this.url = url;
        this.name = name;
        this.isCurrent = isCurrent;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public boolean isCurrent() {
        return isCurrent;
    }
}
