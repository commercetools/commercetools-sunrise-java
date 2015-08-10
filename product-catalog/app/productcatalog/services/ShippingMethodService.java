package productcatalog.services;

import productcatalog.models.RichShippingRate;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Zone;
import productcatalog.models.ShippingMethods;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ShippingMethodService {
    private final List<ShippingMethod> shippingMethods;

    @Inject
    public ShippingMethodService(final ShippingMethods shippingMethods) {
        this.shippingMethods = shippingMethods.shippingMethods;
    }

    public List<RichShippingRate> getShippingRates(final Reference<Zone> zone) {
        return shippingMethods.stream()
                .flatMap(shippingMethod -> shippingMethod.getShippingRatesForZone(zone).stream()
                        .map(shippingRate -> new RichShippingRate(shippingMethod.getName(), shippingRate)))
                .collect(toList());
    }
}
