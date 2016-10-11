package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.models.ViewModel;

import java.util.List;

public class PaymentMethodFormFieldBean extends ViewModel {

    private List<PaymentFormSelectableOptionBean> list;

    public List<PaymentFormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<PaymentFormSelectableOptionBean> list) {
        this.list = list;
    }
}
