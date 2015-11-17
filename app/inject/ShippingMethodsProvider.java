package inject;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import purchase.ShippingMethods;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class ShippingMethodsProvider extends Base implements Provider<ShippingMethods> {

    private final SphereClient client;

    @Inject
    public ShippingMethodsProvider(final SphereClient client) {
        this.client = client;
    }

    @Override
    public ShippingMethods get() {
        final List<ShippingMethod> shippingMethodList = client.execute(ShippingMethodQuery.of().withLimit(500)).toCompletableFuture().join().getResults();
        return ShippingMethods.of(shippingMethodList);
    }
}
