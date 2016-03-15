package common.i18n;

import org.junit.Test;

import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class I18nResolverTest {

    @Test
    public void resolvesTranslation() throws Exception {
        final Optional<String> message = i18nResolverForEnglish().get(asList(Locale.GERMAN, Locale.ENGLISH), "foo", "bar");
        assertThat(message).contains("some sentence");
    }

    @Test
    public void emptyWhenNoLanguageFound() throws Exception {
        final Optional<String> message = i18nResolverForEnglish().get(singletonList(Locale.GERMAN), "foo", "bar");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenNoLanguageDefined() throws Exception {
        final Optional<String> message = i18nResolverForEnglish().get(emptyList(), "foo", "bar");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyStringWhenNoLanguageFound() throws Exception {
        final String message = i18nResolverForEnglish().getOrEmpty(singletonList(Locale.GERMAN), "foo", "bar");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyStringWhenNoLanguageDefined() throws Exception {
        final String message = i18nResolverForEnglish().getOrEmpty(emptyList(), "foo", "bar");
        assertThat(message).isEmpty();
    }

    private I18nResolver i18nResolverForEnglish() {
        return (locales, bundle, key, hashArgs) ->
                locales.contains(Locale.ENGLISH) ? Optional.of("some sentence") : Optional.empty();
    }

}
