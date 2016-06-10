package com.commercetools.sunrise.common.models;

import java.util.List;

public class CategoryBean extends LinkBean {
    private List<CategoryBean> children;
    private boolean sale;

    public CategoryBean() {
    }

    public CategoryBean(final String text, final String url) {
        super(text, url);
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
