package shoppingcart.checkout.shipping;

import shoppingcart.common.CheckoutPageContent;

public class CheckoutShippingPageContent extends CheckoutPageContent {

    private CheckoutShippingFormBean shippingForm;

    public CheckoutShippingPageContent() {
    }

    public CheckoutShippingFormBean getShippingForm() {
        return shippingForm;
    }

    public void setShippingForm(final CheckoutShippingFormBean shippingForm) {
        this.shippingForm = shippingForm;
    }
}
