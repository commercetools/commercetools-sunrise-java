package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public abstract class FacetViewModel extends ViewModel {

    private String label;
    private boolean isAvailable;
    private boolean isCountHidden;

    public FacetViewModel() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(final boolean available) {
        isAvailable = available;
    }

    public boolean isCountHidden() {
        return isCountHidden;
    }

    public void setCountHidden(final boolean countHidden) {
        isCountHidden = countHidden;
    }
}
