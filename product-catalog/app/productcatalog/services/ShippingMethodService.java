package productcatalog.services;

import productcatalog.models.ShopShippingRate;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Zone;
import productcatalog.models.ShippingMethods;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public interface ShippingMethodService {

    /**
     * Gets all shipping rates available for a zone
     * @param zone the zone
     * @return the list of shipping rates
     * */
    public List<ShopShippingRate> getShippingRates(final Reference<Zone> zone);
}
