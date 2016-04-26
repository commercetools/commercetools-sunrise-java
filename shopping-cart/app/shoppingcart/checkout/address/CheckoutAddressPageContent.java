package shoppingcart.checkout.address;

import shoppingcart.checkout.CheckoutPageContent;

public class CheckoutAddressPageContent extends CheckoutPageContent {

    private CheckoutAddressFormBean addressForm;

    public CheckoutAddressPageContent() {
    }

    public CheckoutAddressFormBean getAddressForm() {
        return addressForm;
    }

    public void setAddressForm(final CheckoutAddressFormBean addressForm) {
        this.addressForm = addressForm;
    }
}
