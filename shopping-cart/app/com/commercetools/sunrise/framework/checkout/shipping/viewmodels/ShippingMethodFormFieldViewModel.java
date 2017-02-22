package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class ShippingMethodFormFieldViewModel extends ViewModel {

    private List<ShippingFormSelectableOptionViewModel> list;

    public List<ShippingFormSelectableOptionViewModel> getList() {
        return list;
    }

    public void setList(final List<ShippingFormSelectableOptionViewModel> list) {
        this.list = list;
    }
}
