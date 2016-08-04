package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.models.ModelBean;

import java.util.List;

public class PaymentMethodFormFieldBean extends ModelBean {

    private List<PaymentFormSelectableOptionBean> list;

    public List<PaymentFormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<PaymentFormSelectableOptionBean> list) {
        this.list = list;
    }
}
