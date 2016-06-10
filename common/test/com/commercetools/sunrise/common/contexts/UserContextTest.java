package com.commercetools.sunrise.common.contexts;

import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static com.neovisionaries.i18n.CountryCode.UK;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserContextTest {
    private static final CurrencyUnit GBP = Monetary.getCurrency("GBP");

    @Test
    public void createsUserContext() throws Exception {
        final UserContext userContext = UserContext.of(asList(ENGLISH, GERMAN, FRENCH), UK, GBP, customerGroup(), channel());
        assertThat(userContext.locale()).isEqualTo(ENGLISH);
        assertThat(userContext.locales()).containsExactly(ENGLISH, GERMAN, FRENCH);
        assertThat(userContext.country()).isEqualTo(UK);
        assertThat(userContext.currency()).isEqualTo(GBP);
        assertThat(userContext.customerGroup()).contains(customerGroup());
        assertThat(userContext.channel()).contains((channel()));
    }

    @Test
    public void createsUserContextWithEmptyCustomerGroupAndChannel() throws Exception {
        final UserContext userContext = UserContext.of(singletonList(ENGLISH), UK, GBP);
        assertThat(userContext.locale()).isEqualTo(ENGLISH);
        assertThat(userContext.locales()).containsExactly(ENGLISH);
        assertThat(userContext.country()).isEqualTo(UK);
        assertThat(userContext.currency()).isEqualTo(GBP);
        assertThat(userContext.customerGroup()).isEmpty();
        assertThat(userContext.channel()).isEmpty();
    }

    @Test
    public void throwsExceptionOnEmptyLocales() throws Exception {
        assertThatThrownBy(() -> UserContext.of(emptyList(), UK, GBP))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Locales must contain at least one valid locale");
    }

    @Test
    public void throwsExceptionOnNullFirstLocale() throws Exception {
        assertThatThrownBy(() -> UserContext.of(asList(null, ENGLISH), UK, GBP))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Locales must contain at least one valid locale");
    }

    private Reference<CustomerGroup> customerGroup() {
        return Reference.of(CustomerGroup.referenceTypeId(), "foo");
    }

    private Reference<Channel> channel() {
        return Reference.of(Channel.referenceTypeId(), "foo");
    }
}