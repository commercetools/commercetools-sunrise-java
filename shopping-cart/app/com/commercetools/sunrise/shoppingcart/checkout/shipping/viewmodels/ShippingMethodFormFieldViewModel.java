package com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

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
