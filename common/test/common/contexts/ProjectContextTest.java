package common.contexts;

import com.neovisionaries.i18n.CountryCode;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static com.neovisionaries.i18n.CountryCode.*;
import static com.neovisionaries.i18n.CountryCode.UK;
import static java.util.Arrays.asList;
import static java.util.Locale.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ProjectContextTest {
    private static final List<Locale> AVAILABLE_LANGUAGES = asList(ENGLISH, CHINESE, GERMAN);
    private static final List<CountryCode> AVAILABLE_COUNTRIES = asList(UK, CN, DE);

    @Test
    public void createsProjectContext() throws Exception {
        final ProjectContext projectContext = ProjectContext.of(AVAILABLE_LANGUAGES, AVAILABLE_COUNTRIES);
        assertThat(projectContext.languages()).containsExactlyElementsOf(AVAILABLE_LANGUAGES);
        assertThat(projectContext.countries()).containsExactlyElementsOf(AVAILABLE_COUNTRIES);
    }
}
