package productcatalog.common;

import io.sphere.sdk.models.Base;

public class ProductVariantReferenceBean extends Base {

    private int id;
    private String url;

    public ProductVariantReferenceBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
