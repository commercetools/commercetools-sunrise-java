package common.prices;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceFinderTest {

    private final CurrencyUnit eur = Monetary.getCurrency("EUR");
    private final CurrencyUnit usd = Monetary.getCurrency("USD");

    private final CountryCode de = CountryCode.DE;

    private final Reference<CustomerGroup> group1 = Reference.of(CustomerGroup.referenceTypeId(), "group1");
    private final Reference<Channel> channel1 = Reference.of(Channel.referenceTypeId(), "channel1");

    private final LocalDateTime today = LocalDateTime.now();
    private final LocalDateTime tomorrow = today.plusDays(1);

    private final ZoneId berlin = ZoneId.of("Europe/Berlin");

    private final ZonedDateTime tomorrowDe = ZonedDateTime.of(tomorrow, berlin);
    private final ZonedDateTime todayDe = ZonedDateTime.of(today, berlin);

    private final Price euroPrice = Price.of(BigDecimal.valueOf(10), eur);
    private final Price dollarPrice = Price.of(BigDecimal.valueOf(10), usd);

    private final Price invalidCurrencyPrice = dollarPrice;
    private final Price invalidDatePrice = euroPrice.withValidFrom(tomorrowDe);

    // correct prices that must be found in tests, the one with highest number is always to be prioritized
    private final Price priceWithoutDate = euroPrice;
    private final Price priceWithDate = euroPrice.withValidUntil(tomorrowDe);
    private final Price priceWithCountry = priceWithDate.withCountry(de);
    private final Price priceWithChannel = priceWithDate.withChannel(channel1);
    private final Price priceWithCountryAndChannel = priceWithDate.withCountry(de).withChannel(channel1);
    private final Price priceWithGroup = priceWithDate.withCustomerGroup(group1);
    private final Price priceWithGroupAndCountry = priceWithDate.withCustomerGroup(group1).withCountry(de);
    private final Price priceWithGroupAndChannel = priceWithDate.withCustomerGroup(group1).withChannel(channel1);
    private final Price priceWithCountryGroupAndChannel = priceWithDate.withCountry(de).withCustomerGroup(group1).withChannel(channel1);

    // values that might be in users scope
    private final Optional<Reference<CustomerGroup>> customerGroup = Optional.of(group1);
    private final Optional<Reference<Channel>> channel = Optional.of(channel1);

    private final Optional<Reference<CustomerGroup>> emptyCustomerGroup = Optional.empty();
    private final Optional<Reference<Channel>> emptyChannel = Optional.empty();

    private final PriceFinder priceFinder = PriceFinder.of(eur, de, customerGroup, channel);
    private final PriceFinder priceFinderIncompleteScope = PriceFinder.of(eur, de, emptyCustomerGroup, emptyChannel);

    @Test
    public void findWithCurrencyAndEmptyDate() throws Exception {
        final List<Price> prices = asList(invalidCurrencyPrice, invalidDatePrice, priceWithoutDate);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithoutDate);
    }

    @Test
    public void findWithCurrencyAndDate() throws Exception {
        final List<Price> prices = asList(invalidCurrencyPrice, invalidDatePrice, priceWithoutDate, priceWithDate);

        final Optional<Price> foundPrice =
                priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithDate);
    }

    @Test
    public void findWithCountry() throws Exception {
        final List<Price> prices = asList(
                invalidCurrencyPrice,
                invalidDatePrice,
                priceWithoutDate,
                priceWithDate,
                priceWithCountry);

        final Optional<Price> foundPrice = priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithCountry);
    }

    @Test
    public void findWithChannel() throws Exception {
        final List<Price> prices = asList(
                invalidCurrencyPrice,
                invalidDatePrice,
                priceWithoutDate,
                priceWithDate,
                priceWithCountry,
                priceWithChannel);

        final Optional<Price> foundPrice = priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithChannel);
    }

    @Test
    public void findWithCountryAndChannel() throws Exception {
        final List<Price> prices = asList(invalidCurrencyPrice,
                invalidDatePrice,
                priceWithoutDate,
                priceWithDate,
                priceWithCountry,
                priceWithChannel,
                priceWithCountryAndChannel);

        final Optional<Price> foundPrice = priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithCountryAndChannel);
    }

    @Test
    public void findWithCustomerGroup() throws Exception {
        final List<Price> prices = asList(invalidCurrencyPrice,
                invalidDatePrice,
                priceWithoutDate,
                priceWithDate,
                priceWithCountry,
                priceWithChannel,
                priceWithCountryAndChannel,
                priceWithGroup);

        final Optional<Price> foundPrice = priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithGroup);
    }

    @Test
    public void findWithCustomerGroupAndCountry() throws Exception {
        final List<Price> prices = asList(
                invalidCurrencyPrice,
                invalidDatePrice,
                priceWithoutDate,
                priceWithDate,
                priceWithCountry,
                priceWithChannel,
                priceWithCountryAndChannel,
                priceWithGroup,
                priceWithGroupAndCountry);

        final Optional<Price> foundPrice = priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithGroupAndCountry);
    }

    @Test
    public void findWithCustomerGroupAndChannel() throws Exception {
        final List<Price> prices = asList(
                invalidCurrencyPrice,
                invalidDatePrice,
                priceWithoutDate,
                priceWithDate,
                priceWithCountry,
                priceWithChannel,
                priceWithCountryAndChannel,
                priceWithGroup,
                priceWithGroupAndCountry,
                priceWithGroupAndChannel);

        final Optional<Price> foundPrice = priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithGroupAndChannel);
    }

    @Test
    public void findWithCountryCustomerGroupAndChannel() throws Exception {
        final List<Price> prices = asList(
                invalidCurrencyPrice,
                invalidDatePrice,
                priceWithoutDate,
                priceWithDate,
                priceWithCountry,
                priceWithChannel,
                priceWithCountryAndChannel,
                priceWithGroup,
                priceWithGroupAndCountry,
                priceWithGroupAndChannel,
                priceWithCountryGroupAndChannel);

        final Optional<Price> foundPrice = priceFinder.findPrice(prices);

        assertThat(foundPrice).contains(priceWithCountryGroupAndChannel);
    }

    @Test
    public void findWithCustomerGroupAndChannelWithIncompleteScope() throws Exception {
        final List<Price> prices = asList(invalidCurrencyPrice, invalidDatePrice, priceWithoutDate);

        final Optional<Price> foundPrice = priceFinderIncompleteScope.findPrice(prices);

        assertThat(foundPrice).contains(priceWithoutDate);
    }

}