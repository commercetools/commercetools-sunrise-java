package purchase;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;

public class CheckoutShippingFormBean extends Base {
    private ShippingMethodsFormBean shippingMethods;
    private ErrorsBean errors;


    public CheckoutShippingFormBean() {
    }

    public CheckoutShippingFormBean(final Cart cart, final ShippingMethods shippingMethods) {
        setShippingMethods(new ShippingMethodsFormBean(cart, shippingMethods));
    }

    public CheckoutShippingFormBean(final CheckoutShippingFormData checkoutShippingFormData, final ShippingMethods shippingMethods) {
        setShippingMethods(new ShippingMethodsFormBean(checkoutShippingFormData, shippingMethods));
    }

    public ShippingMethodsFormBean getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(final ShippingMethodsFormBean shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }
}
