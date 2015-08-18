package common.contexts;

import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.zones.Zone;
import org.junit.Test;

import java.time.ZoneId;

import static com.neovisionaries.i18n.CountryCode.UK;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class UserContextTest {

    final Reference<Zone> zone = Reference.of(Zone.typeId(), "f77ddfd4-af5b-471a-89c5-9a40d8a7ab88");
    final ZoneId zoneId = ZoneId.of("Europe/London");

    @Test
    public void createsUserContext() throws Exception {
        final UserContext userContext = UserContext.of(UK, ENGLISH, asList(ENGLISH, GERMAN, FRENCH) , zoneId, zone, customerGroup(), channel());
        assertThat(userContext.country()).isEqualTo(UK);
        assertThat(userContext.language()).isEqualTo(ENGLISH);
        assertThat(userContext.fallbackLanguages()).containsExactly(ENGLISH, GERMAN, FRENCH);
        assertThat(userContext.zoneId()).isEqualTo(zoneId);
        assertThat(userContext.zone()).isEqualTo(zone);
        assertThat(userContext.customerGroup()).contains(customerGroup());
        assertThat(userContext.channel()).contains((channel()));
    }

    @Test
    public void createsUserContextWithEmptyCustomerGroupAndChannel() throws Exception {
        final UserContext userContext = UserContext.of(UK, ENGLISH, emptyList(), zoneId, zone);
        assertThat(userContext.country()).isEqualTo(UK);
        assertThat(userContext.language()).isEqualTo(ENGLISH);
        assertThat(userContext.fallbackLanguages()).isEmpty();
        assertThat(userContext.zoneId()).isEqualTo(zoneId);
        assertThat(userContext.zone()).isEqualTo(zone);
        assertThat(userContext.customerGroup()).isEmpty();
        assertThat(userContext.channel()).isEmpty();
    }

    private Reference<CustomerGroup> customerGroup() {
        return Reference.of(CustomerGroup.typeId(), "foo");
    }

    private Reference<Channel> channel() {
        return Reference.of(Channel.typeId(), "foo");
    }
}