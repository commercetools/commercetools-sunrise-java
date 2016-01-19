package shoppingcart.checkout.payment;

import io.sphere.sdk.models.Base;

public class PaymentsBean extends Base {
    private String type;

    public PaymentsBean() {
    }

    public PaymentsBean(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
