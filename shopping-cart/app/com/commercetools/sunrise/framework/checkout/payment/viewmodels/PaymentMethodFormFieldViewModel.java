package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

import java.util.List;

public class PaymentMethodFormFieldViewModel extends ViewModel {

    private List<PaymentFormSelectableOptionViewModel> list;

    public List<PaymentFormSelectableOptionViewModel> getList() {
        return list;
    }

    public void setList(final List<PaymentFormSelectableOptionViewModel> list) {
        this.list = list;
    }
}
