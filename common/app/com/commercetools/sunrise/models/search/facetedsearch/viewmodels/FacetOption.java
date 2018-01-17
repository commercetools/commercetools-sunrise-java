package com.commercetools.sunrise.models.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.models.SelectOption;

import java.util.List;

public class FacetOption extends SelectOption {

    private Long count;
    private List<FacetOption> children;

    public FacetOption() {
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }

    public List<FacetOption> getChildren() {
        return children;
    }

    public void setChildren(final List<FacetOption> children) {
        this.children = children;
    }
}
