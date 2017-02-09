package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.LocalizedString;

import java.util.List;

public class CategoryBean extends ViewModel {

    private LocalizedString text;
    private String url;
    private boolean selected;
    private List<CategoryBean> children;
    private boolean sale;

    public CategoryBean() {
    }

    public LocalizedString getText() {
        return text;
    }

    public void setText(final LocalizedString text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public List<CategoryBean> getChildren() {
        return children;
    }

    public void setChildren(final List<CategoryBean> children) {
        this.children = children;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(final boolean sale) {
        this.sale = sale;
    }
}
