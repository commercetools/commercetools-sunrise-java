package com.commercetools.sunrise.models;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

public class SelectOption extends ViewModel {

    private String label;
    private String value;
    private Boolean disabled;
    private Boolean selected;

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
