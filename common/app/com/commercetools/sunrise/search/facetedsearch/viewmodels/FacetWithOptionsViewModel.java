package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import java.util.List;

public abstract class FacetWithOptionsViewModel extends FacetViewModel {

    private String key;
    private boolean multiSelect;
    private boolean matchingAll;
    private List<FacetOptionViewModel> limitedOptions;

    public FacetWithOptionsViewModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<FacetOptionViewModel> getLimitedOptions() {
        return limitedOptions;
    }

    public void setLimitedOptions(final List<FacetOptionViewModel> limitedOptions) {
        this.limitedOptions = limitedOptions;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(final boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public boolean isMatchingAll() {
        return matchingAll;
    }

    public void setMatchingAll(final boolean matchingAll) {
        this.matchingAll = matchingAll;
    }
}
