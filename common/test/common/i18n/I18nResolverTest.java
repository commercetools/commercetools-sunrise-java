package common.i18n;

import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class I18nResolverTest {
    private static final List<Locale> SOME_LOCALES = asList(Locale.ENGLISH, Locale.GERMAN);

    @Test
    public void returnsFirstFoundTranslation() throws Exception {
        final I18nResolver i18nResolver = (locale, bundle, key, args) ->
                locale.equals(Locale.GERMAN) ? Optional.of("some sentence") : Optional.empty();
        final Optional<String> message = i18nResolver.get(SOME_LOCALES, "foo", "bar");
        assertThat(message).contains("some sentence");
    }

    @Test
    public void emptyWhenNoLanguageFound() throws Exception {
        final I18nResolver i18nResolver = (locale, bundle, key, args) -> Optional.empty();
        final Optional<String> message = i18nResolver.get(SOME_LOCALES, "foo", "bar");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenNoLanguageDefined() throws Exception {
        final I18nResolver i18nResolver = (locale, bundle, key, args) -> Optional.of("some sentence");
        final Optional<String> message = i18nResolver.get(emptyList(), "foo", "bar");
        assertThat(message).isEmpty();
    }

    @Test
    public void nullWhenLanguageNotFound() throws Exception {
        final I18nResolver i18nResolver = (locale, bundle, key, args) -> Optional.empty();
        final String message = i18nResolver.getOrEmpty(Locale.ENGLISH, "foo", "bar");
        assertThat(message).isEmpty();
    }

    @Test
    public void nullWhenNoLanguageFound() throws Exception {
        final I18nResolver i18nResolver = (locale, bundle, key, args) -> Optional.empty();
        final String message = i18nResolver.getOrEmpty(SOME_LOCALES, "foo", "bar");
        assertThat(message).isEmpty();
    }

}
