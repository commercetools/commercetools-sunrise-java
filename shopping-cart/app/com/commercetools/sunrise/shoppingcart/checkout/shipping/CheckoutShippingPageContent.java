package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.shoppingcart.common.CheckoutPageContent;
import play.data.Form;

public class CheckoutShippingPageContent extends CheckoutPageContent {

    private Form<?> shippingForm;
    private CheckoutShippingFormSettingsBean shippingFormSettings;

    public CheckoutShippingPageContent() {
    }

    public Form<?> getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final Form<?> shippingForm) {
        this.shippingForm = shippingForm;
    }

    public CheckoutShippingFormSettingsBean getShippingFormSettings() {
        return shippingFormSettings;
    }

    public void setShippingFormSettings(final CheckoutShippingFormSettingsBean shippingFormSettings) {
        this.shippingFormSettings = shippingFormSettings;
    }
}
