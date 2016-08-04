package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.models.FormSelectableOptionBean;

public class PaymentFormSelectableOptionBean extends FormSelectableOptionBean {

    private String image;

    public PaymentFormSelectableOptionBean() {
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
