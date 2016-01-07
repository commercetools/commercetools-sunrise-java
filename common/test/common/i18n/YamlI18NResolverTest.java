package common.i18n;

import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class YamlI18nResolverTest {

    @Test
    public void resolvesSimpleKey() throws Exception {
        final Optional<String> message = i18nResolver().get("default", "baz", ENGLISH);
        assertThat(message).contains("this");
    }

    @Test
    public void resolvesNestedKey() throws Exception {
        final Optional<String> message = i18nResolver().get("default", "foo.bar.qux", ENGLISH);
        assertThat(message).contains("that");
    }

    @Test
    public void resolvesSimpleKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = i18nResolver().get("default", "baz", GERMAN);
        assertThat(message).contains("dies");
    }

    @Test
    public void resolvesNestedKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = i18nResolver().get("default", "foo.bar.qux", GERMAN);
        assertThat(message).contains("das");
    }

    @Test
    public void resolvesSimpleKeyInDifferentBundle() throws Exception {
        final Optional<String> message = i18nResolver().get("onlyenglish", "foobar", ENGLISH);
        assertThat(message).contains("something");
    }

    @Test
    public void emptyWhenKeyNotFound() throws Exception {
        final Optional<String> message = i18nResolver().get("default", "unknown", ENGLISH);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFoundOnNestedKey() throws Exception {
        final Optional<String> message = i18nResolver().get("default", "foo.bar.unknown", ENGLISH);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenLanguageNotFound() throws Exception {
        final Optional<String> message = i18nResolver().get("onlyenglish", "foobar", GERMAN);
        assertThat(message).isEmpty();
    }

    private static YamlI18nResolver i18nResolver() {
        final List<Locale> supportedLocales = asList(ENGLISH, GERMAN);
        final List<String> availableBundles = asList("default", "onlyenglish");
        return new YamlI18nResolver("i18n", supportedLocales, availableBundles);
    }
}
