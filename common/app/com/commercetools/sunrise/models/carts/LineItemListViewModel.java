package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

import java.util.List;

public class LineItemListViewModel extends ViewModel {

    private List<LineItemViewModel> list;

    public LineItemListViewModel() {
    }

    public List<LineItemViewModel> getList() {
        return list;
    }

    public void setList(final List<LineItemViewModel> list) {
        this.list = list;
    }
}
