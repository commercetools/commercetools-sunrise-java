package productcatalog.services;

import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import play.libs.F;

import javax.inject.Inject;
import java.util.List;

public class ShippingMethodService {

    private final PlayJavaSphereClient sphere;

    @Inject
    public ShippingMethodService(final PlayJavaSphereClient sphere) {
        this.sphere = sphere;
    }

    public F.Promise<List<ShippingMethod>> getShippingMethods() {
        final ShippingMethodQuery shippingMethodQuery = ShippingMethodQuery.of();
        return sphere.execute(shippingMethodQuery).map(PagedResult::getResults);
    }
}
