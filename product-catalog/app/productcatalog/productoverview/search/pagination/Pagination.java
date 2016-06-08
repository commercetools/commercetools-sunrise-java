package productcatalog.productoverview.search.pagination;

import io.sphere.sdk.models.Base;

public final class Pagination extends Base {

    private final String key;
    private final int page;

    private Pagination(final String key, final int page) {
        this.key = key;
        this.page = page;
    }

    public String getKey() {
        return key;
    }

    public int getPage() {
        return page;
    }

    public static Pagination of(final String key, final int page) {
        return new Pagination(key, page);
    }
}