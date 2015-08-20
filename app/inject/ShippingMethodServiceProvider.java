package inject;

import com.google.inject.Provider;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import play.Logger;
import productcatalog.models.ShippingMethods;
import productcatalog.services.ShippingMethodService;
import productcatalog.services.ShippingMethodServiceImpl;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ShippingMethodServiceProvider implements Provider<ShippingMethodService> {

    private SphereClient client;

    @Inject
    public ShippingMethodServiceProvider(final SphereClient client) {
        this.client = client;
    }

    @Override
    public ShippingMethodService get() {
        try {
            final List<ShippingMethod> shippingMethods = client.execute(ShippingMethodQuery.of()).toCompletableFuture().get().getResults();
            Logger.debug("Provide " + shippingMethods.size() + " ShippingMethods");
            return new ShippingMethodServiceImpl(ShippingMethods.of(shippingMethods));
        } catch (InterruptedException | ExecutionException e) {
            throw new SunriseInitializationException("Could not fetch shippingMethods", e);
        }
    }
}
