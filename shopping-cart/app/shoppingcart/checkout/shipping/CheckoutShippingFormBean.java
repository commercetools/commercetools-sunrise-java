package shoppingcart.checkout.shipping;

import common.errors.ErrorsBean;
import io.sphere.sdk.carts.CartShippingInfo;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.annotation.Nullable;
import java.util.List;

public class CheckoutShippingFormBean extends Base {

    private ShippingMethodsFormBean shippingMethods;
    private ErrorsBean errors;

    public CheckoutShippingFormBean() {
    }

    public CheckoutShippingFormBean(final List<ShippingMethod> shippingMethods, final @Nullable CartShippingInfo shippingInfo) {
        this.shippingMethods = new ShippingMethodsFormBean(shippingMethods, shippingInfo);
    }

    public CheckoutShippingFormBean(final List<ShippingMethod> shippingMethods, final Form<CheckoutShippingFormData> shippingForm) {
        final String shippingMethodId = shippingForm.field("shippingMethodId").value();
        this.shippingMethods = new ShippingMethodsFormBean(shippingMethods, shippingMethodId);
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
