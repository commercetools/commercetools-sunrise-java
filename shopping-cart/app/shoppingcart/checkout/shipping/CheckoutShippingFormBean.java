package shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.errors.ErrorsBean;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.annotation.Nullable;
import java.util.List;

public class CheckoutShippingFormBean extends Base {

    private ShippingMethodsFormBean shippingMethods;
    private ErrorsBean errors;

    public CheckoutShippingFormBean() {
    }

    public CheckoutShippingFormBean(final List<ShippingMethod> shippingMethods, final @Nullable String selectedShippingMethodId) {
        this.shippingMethods = new ShippingMethodsFormBean(shippingMethods, selectedShippingMethodId);
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
