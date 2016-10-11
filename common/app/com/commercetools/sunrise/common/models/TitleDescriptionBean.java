package com.commercetools.sunrise.common.models;

public class TitleDescriptionBean extends ViewModel {

    private String title;
    private String description;

    public TitleDescriptionBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
