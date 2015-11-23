package productcatalog.models;

import common.utils.PriceFormatter;
import io.sphere.sdk.shippingmethods.ShippingRate;
import org.javamoney.moneta.Money;
import org.junit.Test;
import productcatalog.models.ShippingRateData;
import productcatalog.models.ShippingRateDataFactory;
import productcatalog.models.ShopShippingRate;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class ShippingRateDataFactoryTest {
    private static final CurrencyUnit EUR = Monetary.getCurrency("EUR");
    private static final PriceFormatter PRICE_FORMATTER = PriceFormatter.of(GERMAN);

    @Test
    public void create() {
        final ShopShippingRate shippingRate = new ShopShippingRate("DHL", ShippingRate.of(Money.of(10.50, EUR), Money.of(50.49, EUR)));

        final ShippingRateData shippingRateData =
                ShippingRateDataFactory.of(PRICE_FORMATTER).create(shippingRate);

        assertThat(shippingRateData.getShippingMethod()).isEqualTo("DHL");
        assertThat(shippingRateData.getZone()).isEqualTo("");
        assertThat(shippingRateData.getShippingRate()).isEqualTo("EUR 10,50");
        assertThat(shippingRateData.getFreeAbove()).isEqualTo("EUR 50,49");
    }
}
