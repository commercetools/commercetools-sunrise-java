package common.pages;

import java.util.List;

public class CategoryData extends LinkData {
    private final List<CategoryData> children;
    private final boolean sale;

    public CategoryData(final String text, final String url, final List<CategoryData> children, final boolean sale) {
        super(text, url);
        this.children = children;
        this.sale = sale;
    }

    public List<CategoryData> getChildren() {
        return children;
    }

    public boolean isSale() {
        return sale;
    }
}
