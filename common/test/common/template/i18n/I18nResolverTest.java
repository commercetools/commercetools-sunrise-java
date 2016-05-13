package common.template.i18n;

import org.junit.Test;

import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class I18nResolverTest {

    private static final I18nIdentifier I18N_IDENTIFIER = I18nIdentifier.ofBundleAndKey("foo", "bar");

    @Test
    public void resolvesTranslation() throws Exception {
        final Optional<String> message = i18nOnlyForEnglish().get(asList(Locale.GERMAN, Locale.ENGLISH), I18N_IDENTIFIER);
        assertThat(message).contains("some sentence");
    }

    @Test
    public void emptyWhenNoLanguageFound() throws Exception {
        final Optional<String> message = i18nOnlyForEnglish().get(singletonList(Locale.GERMAN), I18N_IDENTIFIER);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenNoLanguageDefined() throws Exception {
        final Optional<String> message = i18nOnlyForEnglish().get(emptyList(), I18N_IDENTIFIER);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyStringWhenNoLanguageFound() throws Exception {
        final String message = i18nOnlyForEnglish().getOrEmpty(singletonList(Locale.GERMAN), I18N_IDENTIFIER);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyStringWhenNoLanguageDefined() throws Exception {
        final String message = i18nOnlyForEnglish().getOrEmpty(emptyList(), I18N_IDENTIFIER);
        assertThat(message).isEmpty();
    }

    @Test
    public void keyWhenNoLanguageFound() throws Exception {
        final String message = i18nOnlyForEnglish().getOrKey(singletonList(Locale.GERMAN), I18N_IDENTIFIER);
        assertThat(message).isEqualTo(I18N_IDENTIFIER.getMessageKey());
    }

    @Test
    public void keyWhenNoLanguageDefined() throws Exception {
        final String message = i18nOnlyForEnglish().getOrKey(emptyList(), I18N_IDENTIFIER);
        assertThat(message).isEqualTo(I18N_IDENTIFIER.getMessageKey());
    }

    @Test
    public void keyWhenNoMessageKeyProvided() throws Exception {
        final String message = i18nOnlyForEnglish().getOrKey(emptyList(), I18nIdentifier.of("some text"));
        assertThat(message).isEqualTo("some text");
    }

    private I18nResolver i18nOnlyForEnglish() {
        return (locales, i18nIdentifier, hashArgs) ->
                locales.contains(Locale.ENGLISH) ? Optional.of("some sentence") : Optional.empty();
    }

}
