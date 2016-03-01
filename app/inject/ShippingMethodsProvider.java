package inject;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import shoppingcart.checkout.shipping.ShippingMethods;

import javax.inject.Inject;
import java.util.List;

class ShippingMethodsProvider extends SphereSmallCollectionProvider<ShippingMethods, ShippingMethod, ShippingMethodQuery> {

    @Inject
    public ShippingMethodsProvider(final SphereClient client) {
        super(client);
    }

    @Override
    protected ShippingMethodQuery query() {
        return ShippingMethodQuery.of();
    }

    @Override
    protected ShippingMethods transform(final List<ShippingMethod> list) {
        return  new ShippingMethods(list);
    }
}
