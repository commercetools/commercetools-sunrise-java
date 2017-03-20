package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSelectableOptionViewModel;

import java.util.List;

public class FacetOptionViewModel extends FormSelectableOptionViewModel {

    private Long count;
    private List<FacetOptionViewModel> children;

    public FacetOptionViewModel() {
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    public List<FacetOptionViewModel> getChildren() {
        return children;
    }

    public void setChildren(final List<FacetOptionViewModel> children) {
        this.children = children;
    }
}
