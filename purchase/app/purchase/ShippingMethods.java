package purchase;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;

public class ShippingMethods extends Base {
    public final List<ShippingMethod> shippingMethods;

    private ShippingMethods(final List<ShippingMethod> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public static ShippingMethods of(final List<ShippingMethod> shippingMethods) {
        return  new ShippingMethods(shippingMethods);
    }
}
