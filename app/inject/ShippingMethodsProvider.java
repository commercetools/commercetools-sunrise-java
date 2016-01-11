package inject;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.MetaModelQueryDsl;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import shoppingcart.shipping.ShippingMethods;

import javax.inject.Inject;
import java.util.List;

class ShippingMethodsProvider extends SphereSmallCollectionProvider<ShippingMethods, ShippingMethod> {

    @Inject
    public ShippingMethodsProvider(final SphereClient client) {
        super(client);
    }

    @Override
    protected MetaModelQueryDsl<ShippingMethod, ?, ?, ?> query() {
        return ShippingMethodQuery.of();
    }

    @Override
    protected ShippingMethods transform(final List<ShippingMethod> list) {
        return  new ShippingMethods(list);
    }
}
