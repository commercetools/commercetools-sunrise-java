package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.common.models.ViewModel;

public class PaginationLinkBean extends ViewModel {

    private String text;
    private String url;
    private boolean selected;

    public PaginationLinkBean() {
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
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
}
