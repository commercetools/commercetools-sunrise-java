package productcatalog.services;

import io.sphere.sdk.models.Reference;
import io.sphere.sdk.zones.Zone;
import productcatalog.models.ShopShippingRate;

import java.util.List;

public interface ShippingMethodService {

    /**
     * Gets all shipping rates available for a zone
     * @param zone the zone
     * @return the list of shipping rates
     * */
    List<ShopShippingRate> getShippingRates(final Reference<Zone> zone);
}
