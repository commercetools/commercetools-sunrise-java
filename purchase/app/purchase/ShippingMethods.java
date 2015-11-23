package purchase;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;

public class ShippingMethods extends Base {
    private final List<ShippingMethod> shippingMethods;

    public ShippingMethods(final List<ShippingMethod> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    public List<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }
}
