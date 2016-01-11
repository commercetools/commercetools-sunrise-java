package shoppingcart.shipping;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class CheckoutShippingFormData extends Base {
    public interface Validation {}

    private String csrfToken;

    @Constraints.Required(groups = Validation.class)
    private String shippingMethodId;

    public CheckoutShippingFormData() {
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(final String shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }
}
