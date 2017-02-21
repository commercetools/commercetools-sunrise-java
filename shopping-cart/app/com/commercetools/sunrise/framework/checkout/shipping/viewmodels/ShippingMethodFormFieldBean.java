package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import java.util.List;

public class ShippingMethodFormFieldBean {

    private List<ShippingFormSelectableOptionBean> list;

    public List<ShippingFormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<ShippingFormSelectableOptionBean> list) {
        this.list = list;
    }
}
