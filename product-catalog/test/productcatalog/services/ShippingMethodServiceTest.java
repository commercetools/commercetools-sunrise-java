package productcatalog.services;

import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.ShippingRate;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodQuery;
import io.sphere.sdk.zones.Zone;
import org.javamoney.moneta.Money;
import org.junit.Test;
import productcatalog.models.ShopShippingRate;
import productcatalog.models.ShippingMethods;

import javax.money.Monetary;
import java.util.List;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;
import static org.assertj.core.api.Assertions.assertThat;

public class ShippingMethodServiceTest {
    private final List<ShippingMethod> shippingMethods = readObjectFromResource("shippingMethodQueryResult.json", ShippingMethodQuery.resultTypeReference()).getResults();
    private final ShippingMethodService service = new ShippingMethodService(ShippingMethods.of(shippingMethods));

    @Test
    public void testGetShippingRates() {
        final Reference<Zone> europe = Reference.of(Zone.typeId(), "f77ddfd4-af5b-471a-89c5-9a40d8a7ab88");
        final Reference<Zone> usa = Reference.of(Zone.typeId(), "67a107d7-e485-4802-a1bd-a475b4394124");
        final Reference<Zone> notExistend = Reference.of(Zone.typeId(), "...");

        final ShopShippingRate euRate = new ShopShippingRate("DHL", ShippingRate.of(Money.of(5.70, Monetary.getCurrency("EUR"))));
        final ShopShippingRate usRate = new ShopShippingRate("DHL", ShippingRate.of(Money.of(9.90, Monetary.getCurrency("USD"))));

        assertThat(service.getShippingRates(europe)).containsExactly(euRate);
        assertThat(service.getShippingRates(usa)).containsExactly(usRate);
        assertThat(service.getShippingRates(notExistend)).isEmpty();
    }
}
