package com.commercetools.sunrise.models.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

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
