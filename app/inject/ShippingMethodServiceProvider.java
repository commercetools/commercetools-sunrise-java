package inject;

import com.google.inject.Provider;
import productcatalog.models.ShippingMethods;
import productcatalog.services.ShippingMethodService;
import productcatalog.services.ShippingMethodServiceImpl;

import javax.inject.Inject;

public class ShippingMethodServiceProvider implements Provider<ShippingMethodService> {
    private final ShippingMethods shippingMethods;

    @Inject
    public ShippingMethodServiceProvider(final ShippingMethods shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

    @Override
    public ShippingMethodService get() {
        return new ShippingMethodServiceImpl(shippingMethods);
    }
}
