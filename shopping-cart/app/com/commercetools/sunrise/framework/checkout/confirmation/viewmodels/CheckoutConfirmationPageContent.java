package com.commercetools.sunrise.framework.checkout.confirmation.viewmodels;

import com.commercetools.sunrise.framework.checkout.AbstractCheckoutPageContent;
import play.data.Form;

public class CheckoutConfirmationPageContent extends AbstractCheckoutPageContent {

    private Form<?> checkoutForm;

    public Form<?> getCheckoutForm() {
        return checkoutForm;
    }

    public void setCheckoutForm(final Form<?> checkoutForm) {
        this.checkoutForm = checkoutForm;
    }
}
