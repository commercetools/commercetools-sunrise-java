package com.commercetools.sunrise.framework.viewmodels.content.categories;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import io.sphere.sdk.models.LocalizedString;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private LocalizedString text;
    private String url;
    private boolean selected;
    private List<CategoryViewModel> children;
    private boolean sale;

    public CategoryViewModel() {
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

    public List<CategoryViewModel> getChildren() {
        return children;
    }

    public void setChildren(final List<CategoryViewModel> children) {
        this.children = children;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(final boolean sale) {
        this.sale = sale;
    }
}
