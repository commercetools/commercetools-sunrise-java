package productcatalog.services;

import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Zone;
import productcatalog.models.ShippingMethods;
import productcatalog.models.ShopShippingRate;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ShippingMethodServiceImpl implements ShippingMethodService {
    private final List<ShippingMethod> shippingMethods;

    @Inject
    public ShippingMethodServiceImpl(final ShippingMethods shippingMethods) {
        this.shippingMethods = shippingMethods.shippingMethods;
    }

    public List<ShopShippingRate> getShippingRates(final Reference<Zone> zone) {
        return shippingMethods.stream()
                .flatMap(shippingMethod -> shippingMethod.getShippingRatesForZone(zone).stream()
                        .map(shippingRate -> new ShopShippingRate(shippingMethod.getName(), shippingRate)))
                .collect(toList());
    }
}
