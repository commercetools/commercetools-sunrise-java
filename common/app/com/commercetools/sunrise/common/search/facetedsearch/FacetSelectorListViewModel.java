package com.commercetools.sunrise.common.search.facetedsearch;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class FacetSelectorListViewModel extends ViewModel {

    private List<FacetSelectorViewModel> list;

    public FacetSelectorListViewModel() {
    }

    public List<FacetSelectorViewModel> getList() {
        return list;
    }

    public void setList(final List<FacetSelectorViewModel> list) {
        this.list = list;
    }
}
