package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import productcatalog.models.ShippingMethods;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Singleton
public class ShippingMethodProvider implements Provider<ShippingMethods> {
    private final SphereClient client;

    @Inject
    private ShippingMethodProvider(final SphereClient client) {
        this.client = client;
    }

    @Override
    public ShippingMethods get() {
        try {
            final List<ShippingMethod> shippingMethods = client.execute(ShippingMethodQuery.of()).toCompletableFuture().get().getResults();
            Logger.debug("Provide "  + shippingMethods.size() + " ShippingMethods");
            return ShippingMethods.of(shippingMethods);
        } catch (InterruptedException | ExecutionException e) {
            throw new SunriseInitializationException("Could not fetch shippingMethods", e);
        }
    }
}
