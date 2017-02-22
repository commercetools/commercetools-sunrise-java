package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.common.models.FormSelectableOptionViewModel;

public class PaymentFormSelectableOptionViewModel extends FormSelectableOptionViewModel {

    private String image;

    public PaymentFormSelectableOptionViewModel() {
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(final String image) {
        this.image = image;
    }
}
