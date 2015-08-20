package productcatalog.pages;

import common.utils.PriceFormatter;
import io.sphere.sdk.shippingmethods.ShippingRate;
import org.javamoney.moneta.Money;
import org.junit.Test;
import productcatalog.models.ShopShippingRate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class ShippingRateDataFactoryTest {
    private final CurrencyUnit eur = Monetary.getCurrency("EUR");
    private final PriceFormatter priceFormatter = PriceFormatter.of(Locale.GERMAN);

    @Test
    public void create() {
        final ShopShippingRate shippingRate = new ShopShippingRate("DHL", ShippingRate.of(Money.of(10.50, eur), Money.of(50.49, eur)));

        final ShippingRateData shippingRateData =
                ShippingRateDataFactory.of(priceFormatter).create(shippingRate);

        assertThat(shippingRateData.getShippingMethod()).isEqualTo("DHL");
        assertThat(shippingRateData.getZone()).isEqualTo("");
        assertThat(shippingRateData.getShippingRate()).isEqualTo("EUR 10,50");
        assertThat(shippingRateData.getFreeAbove()).isEqualTo("EUR 50,49");
    }
}
