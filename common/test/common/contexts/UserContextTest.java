package common.contexts;

import com.neovisionaries.i18n.CountryCode;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class UserContextTest {
    private static final Locale SELECTED_LANGUAGE = Locale.ENGLISH;
    private static final CountryCode SELECTED_COUNTRY = CountryCode.UK;

    @Test
    public void createsProjectContext() throws Exception {
        final UserContext userContext = UserContext.of(SELECTED_LANGUAGE, SELECTED_COUNTRY);
        assertThat(userContext.language()).isEqualTo(SELECTED_LANGUAGE);
        assertThat(userContext.country()).isEqualTo(SELECTED_COUNTRY);
    }
}