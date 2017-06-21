package com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.forms.FormSelectableOptionViewModel;

public class PaymentFormSelectableOptionViewModel extends FormSelectableOptionViewModel {

    private String image;

    public PaymentFormSelectableOptionViewModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }
}
