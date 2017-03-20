package com.commercetools.sunrise.search.pagination.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class PaginationLinkViewModel extends ViewModel {

    private String text;
    private String url;
    private boolean selected;

    public PaginationLinkViewModel() {
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
