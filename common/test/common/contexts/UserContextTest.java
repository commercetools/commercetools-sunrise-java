package common.contexts;

import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import org.junit.Test;

import javax.money.Monetary;
import java.time.ZoneId;

import static com.neovisionaries.i18n.CountryCode.UK;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserContextTest {

    final ZoneId zoneId = ZoneId.of("Europe/London");

    @Test
    public void createsUserContext() throws Exception {
        final UserContext userContext = UserContext.of(UK, asList(ENGLISH, GERMAN, FRENCH) , zoneId, Monetary.getCurrency("EUR"), customerGroup(), channel());
        assertThat(userContext.country()).isEqualTo(UK);
        assertThat(userContext.locale()).isEqualTo(ENGLISH);
        assertThat(userContext.locales()).containsExactly(ENGLISH, GERMAN, FRENCH);
        assertThat(userContext.zoneId()).isEqualTo(zoneId);
        assertThat(userContext.customerGroup()).contains(customerGroup());
        assertThat(userContext.channel()).contains((channel()));
    }

    @Test
    public void createsUserContextWithEmptyCustomerGroupAndChannel() throws Exception {
        final UserContext userContext = UserContext.of(UK, singletonList(ENGLISH), zoneId, Monetary.getCurrency("EUR"));
        assertThat(userContext.country()).isEqualTo(UK);
        assertThat(userContext.locale()).isEqualTo(ENGLISH);
        assertThat(userContext.locales()).containsExactly(ENGLISH);
        assertThat(userContext.zoneId()).isEqualTo(zoneId);
        assertThat(userContext.customerGroup()).isEmpty();
        assertThat(userContext.channel()).isEmpty();
    }

    @Test
    public void throwsExceptionOnEmptyLocales() throws Exception {
        assertThatThrownBy(() -> UserContext.of(UK, emptyList(), zoneId, Monetary.getCurrency("EUR")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Locales must contain at least one valid locale");
    }

    @Test
    public void throwsExceptionOnNullFirstLocale() throws Exception {
        assertThatThrownBy(() -> UserContext.of(UK, asList(null, ENGLISH), zoneId, Monetary.getCurrency("EUR")))
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