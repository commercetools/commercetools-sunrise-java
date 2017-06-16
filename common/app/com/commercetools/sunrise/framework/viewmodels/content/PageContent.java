package com.commercetools.sunrise.framework.viewmodels.content;

import com.commercetools.sunrise.framework.components.viewmodels.ViewModelComponent;
import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.wishlist.WishlistViewModel;

import java.util.LinkedList;
import java.util.List;

public abstract class PageContent extends ViewModel {

    private String title;
    private List<ViewModelComponent> components;
    private WishlistViewModel wishlist;

    public PageContent() {
    }

    public final String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<ViewModelComponent> getComponents() {
        return components;
    }

    public void setComponents(final List<ViewModelComponent> components) {
        this.components = components;
    }

    public void addComponent(final ViewModelComponent component) {
        if (this.components == null) {
            this.components = new LinkedList<>();
        }
        this.components.add(component);
    }
}
