package common.prices;

import com.google.common.base.Optional;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;
import org.javamoney.moneta.CurrencyUnitBuilder;
import org.junit.Test;

import javax.money.CurrencyContext;
import javax.money.CurrencyContextBuilder;
import javax.money.CurrencyUnit;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceFinderTest {

    private final PriceFinder priceFinder = PriceFinder.of();

    private final CurrencyContext eurContext = CurrencyContextBuilder.of("EUR").build();
    private final CurrencyContext usdContext = CurrencyContextBuilder.of("USD").build();

    private final CurrencyUnit eur = CurrencyUnitBuilder.of("EUR", eurContext).build();
    private final CurrencyUnit usd = CurrencyUnitBuilder.of("USD", usdContext).build();

    private final CountryCode de = CountryCode.DE;

    private final Reference<CustomerGroup> group1 = Reference.of(CustomerGroup.typeId(), "group1");
    private final Reference<Channel> channel1 = Reference.of(Channel.typeId(), "channel1");

    private final LocalDateTime today = LocalDateTime.now();
    private final LocalDateTime tomorrow = today.plusDays(1);

    private final ZoneId berlin = ZoneId.of("Europe/Berlin");

    private final ZonedDateTime tomorrowDe = ZonedDateTime.of(tomorrow, berlin);

    private final Price euroPrice = Price.of(BigDecimal.valueOf(10), eur);
    private final Price dollarPrice = Price.of(BigDecimal.valueOf(10), usd);

    private final Price invalidCurrencyPrice = dollarPrice;
    private final Price invalidDatePrice = euroPrice.withValidFrom(tomorrowDe);

    // correct prices that must be found in tests, the one with highest number is always to be prioritized
    private final Price p1 = euroPrice;
    private final Price p2 = euroPrice.withValidUntil(tomorrowDe);
    private final Price p3 = p2.withCountry(de);
    private final Price p4 = p2.withChannel(channel1);
    private final Price p5 = p2.withCountry(de).withChannel(channel1);
    private final Price p6 = p2.withCustomerGroup(group1);
    private final Price p7 = p2.withCustomerGroup(group1).withCountry(de);
    private final Price p8 = p2.withCustomerGroup(group1).withChannel(channel1);
    private final Price p9 = p2.withCountry(de).withCustomerGroup(group1).withChannel(channel1);

    // values that might be in users scope
    private final CurrencyUnit currency = eur;
    private final CountryCode country = de;
    private final java.util.Optional<Reference<CustomerGroup>> customerGroup = java.util.Optional.of(group1);
    private final java.util.Optional<Reference<Channel>> channel = java.util.Optional.of(channel1);
    private final ZonedDateTime userTime = ZonedDateTime.of(today, berlin);

    @Test
    public void findWithCurrencyAndEmptyDate() throws Exception {
        final List<Price> prices = Arrays.asList(p1, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p1));
    }

    @Test
    public void findWithCurrencyAndDate() throws Exception {
        final List<Price> prices = Arrays.asList(p1, p2, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p2));
    }

    @Test
    public void findWithCountry() throws Exception {
        final List<Price> prices = Arrays.asList(p1, p2, p3, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p3));
    }

    @Test
    public void findWithChannel() throws Exception {
        final List<Price> prices = Arrays.asList(p1, p2, p3, p4, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p4));
    }

    @Test
    public void findWithCountryAndChannel() throws Exception {
        final List<Price> prices = Arrays.asList(p1, p2, p3, p4, p5, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p5));
    }

    @Test
    public void findWithCustomerGroup() throws Exception {
        final List<Price> prices = Arrays.asList(p1, p2, p3, p4, p5, p6, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p6));
    }

    @Test
    public void findWithCustomerGroupAndCountry() throws Exception {
        final List<Price> prices = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p7));
    }

    @Test
    public void findWithCustomerGroupAndChannel() throws Exception {
        final List<Price> prices =
                Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p8));
    }

    @Test
    public void findExactMatch() throws Exception {
        final List<Price> prices =
                Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, invalidCurrencyPrice, invalidDatePrice);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices, currency, country, customerGroup, channel, userTime);

        assertThat(foundPrice.isPresent());
        assertThat(foundPrice.get().equals(p9));
    }
}