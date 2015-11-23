package common.pages;

public class PageMeta {
    private String assetsPath;
    private String csrfToken;

    public PageMeta() {
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public void setAssetsPath(final String assetsPath) {
        this.assetsPath = assetsPath;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }
}