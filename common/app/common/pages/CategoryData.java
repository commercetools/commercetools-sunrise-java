package common.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class CategoryData extends Base {
    private final String text;
    private final String url;
    private final List<CategoryData> children;
    private final boolean sale;

    public CategoryData(final String text, final String url, final List<CategoryData> children, final boolean sale) {
        this.text = text;
        this.url = url;
        this.children = children;
        this.sale = sale;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public List<CategoryData> getChildren() {
        return children;
    }

    public boolean isSale() {
        return sale;
    }
}
