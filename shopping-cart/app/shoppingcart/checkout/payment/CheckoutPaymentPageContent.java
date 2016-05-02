package shoppingcart.checkout.payment;

import shoppingcart.checkout.CheckoutPageContent;

public class CheckoutPaymentPageContent extends CheckoutPageContent {

    private CheckoutPaymentFormBean paymentForm;

    public CheckoutPaymentPageContent() {
    }

    public CheckoutPaymentFormBean getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(final CheckoutPaymentFormBean paymentForm) {
        this.paymentForm = paymentForm;
    }
}
