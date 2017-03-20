package com.commercetools.sunrise.framework.viewmodels.forms;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public abstract class FormSelectableOptionViewModel extends ViewModel {

    private String label;
    private String value;
    private boolean selected;

    public FormSelectableOptionViewModel() {
    }

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
