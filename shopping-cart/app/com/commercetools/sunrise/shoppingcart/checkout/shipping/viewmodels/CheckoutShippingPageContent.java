package com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels;

import com.commercetools.sunrise.shoppingcart.checkout.AbstractCheckoutPageContent;
import play.data.Form;

public class CheckoutShippingPageContent extends AbstractCheckoutPageContent {

    private Form<?> shippingForm;
    private CheckoutShippingFormSettingsViewModel shippingFormSettings;

    public CheckoutShippingPageContent() {
    }

    public Form<?> getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final Form<?> shippingForm) {
        this.shippingForm = shippingForm;
    }

    public CheckoutShippingFormSettingsViewModel getShippingFormSettings() {
        return shippingFormSettings;
    }

    public void setShippingFormSettings(final CheckoutShippingFormSettingsViewModel shippingFormSettings) {
        this.shippingFormSettings = shippingFormSettings;
    }
}
