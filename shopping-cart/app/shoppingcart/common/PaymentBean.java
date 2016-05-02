package shoppingcart.common;

import io.sphere.sdk.models.Base;

public class PaymentBean extends Base {

    private String type;

    public PaymentBean() {
    }

    public PaymentBean(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
