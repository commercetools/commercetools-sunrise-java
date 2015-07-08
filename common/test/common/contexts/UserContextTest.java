package common.contexts;

import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import org.junit.Test;

import static com.neovisionaries.i18n.CountryCode.UK;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class UserContextTest {

    @Test
    public void createsUserContext() throws Exception {
        final UserContext userContext = UserContext.of(ENGLISH, asList(ENGLISH, GERMAN, FRENCH), UK, customerGroup(), channel());
        assertThat(userContext.language()).isEqualTo(ENGLISH);
        assertThat(userContext.fallbackLanguages()).containsExactly(ENGLISH, GERMAN, FRENCH);
        assertThat(userContext.country()).isEqualTo(UK);
        assertThat(userContext.customerGroup()).contains(customerGroup());
        assertThat(userContext.channel()).contains((channel()));
    }

    @Test
    public void createsUserContextWithEmptyCustomerGroupAndChannel() throws Exception {
        final UserContext userContext = UserContext.of(ENGLISH, emptyList(), UK);
        assertThat(userContext.language()).isEqualTo(ENGLISH);
        assertThat(userContext.fallbackLanguages()).isEmpty();
        assertThat(userContext.country()).isEqualTo(UK);
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