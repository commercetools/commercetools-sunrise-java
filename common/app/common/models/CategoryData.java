package common.models;

import java.util.List;

public class CategoryData extends LinkData {
    private List<CategoryData> children;
    private boolean sale;

    public CategoryData() {
    }

    public CategoryData(final String text, final String url) {
        super(text, url);
    }

    public List<CategoryData> getChildren() {
        return children;
    }

    public void setChildren(final List<CategoryData> children) {
        this.children = children;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(final boolean sale) {
        this.sale = sale;
    }
}
