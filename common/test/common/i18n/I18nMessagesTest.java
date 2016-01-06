package common.i18n;

import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class I18nMessagesTest {
    private static final List<Locale> SOME_LOCALES = asList(Locale.ENGLISH, Locale.GERMAN);

    @Test
    public void returnsFirstFoundTranslation() throws Exception {
        final I18nMessages i18nMessages = (bundle, key, locale) ->
                locale.equals(Locale.GERMAN) ? Optional.of("some sentence") : Optional.empty();
        final Optional<String> message = i18nMessages.get("foo", "bar", SOME_LOCALES);
        assertThat(message).contains("some sentence");
    }

    @Test
    public void emptyWhenNoLanguageFound() throws Exception {
        final I18nMessages i18nMessages = (bundle, key, locale) -> Optional.empty();
        final Optional<String> message = i18nMessages.get("foo", "bar", SOME_LOCALES);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenNoLanguageDefined() throws Exception {
        final I18nMessages i18nMessages = (bundle, key, locale) -> Optional.of("some sentence");
        final Optional<String> message = i18nMessages.get("foo", "bar", emptyList());
        assertThat(message).isEmpty();
    }
}
