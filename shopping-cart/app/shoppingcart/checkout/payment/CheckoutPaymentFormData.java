package shoppingcart.checkout.payment;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutPaymentFormData extends Base {

    private String csrfToken;

    @Constraints.Required
    private String payment;

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(final String payment) {
        this.payment = payment;
    }
}
