package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.ModelBean;
import com.commercetools.sunrise.components.ComponentBean;

import java.util.LinkedList;
import java.util.List;

public abstract class PageContent extends ModelBean {

    private String title;
    private List<ComponentBean> components;

    public PageContent() {
    }

    public final String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<ComponentBean> getComponents() {
        return components;
    }

    public void setComponents(final List<ComponentBean> components) {
        this.components = components;
    }

    public void addComponent(final ComponentBean component) {
        if (this.components == null) {
            this.components = new LinkedList<>();
        }
        this.components.add(component);
    }
}
