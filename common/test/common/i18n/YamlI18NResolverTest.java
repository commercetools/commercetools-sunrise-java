package common.i18n;

import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class YamlI18NResolverTest {

    @Test
    public void resolvesSimpleKey() throws Exception {
        final Optional<String> message = i18n().resolve("translations", "baz", ENGLISH);
        assertThat(message).contains("this");
    }

    @Test
    public void resolvesNestedKey() throws Exception {
        final Optional<String> message = i18n().resolve("translations", "foo.bar.qux", ENGLISH);
        assertThat(message).contains("that");
    }

    @Test
    public void resolvesSimpleKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = i18n().resolve("translations", "baz", GERMAN);
        assertThat(message).contains("dies");
    }

    @Test
    public void resolvesNestedKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = i18n().resolve("translations", "foo.bar.qux", GERMAN);
        assertThat(message).contains("das");
    }

    @Test
    public void resolvesSimpleKeyInDifferentBundle() throws Exception {
        final Optional<String> message = i18n().resolve("somebundle", "foobar", ENGLISH);
        assertThat(message).contains("something");
    }

    @Test
    public void emptyWhenKeyNotFound() throws Exception {
        final Optional<String> message = i18n().resolve("translations", "unknown", ENGLISH);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFoundOnNestedKey() throws Exception {
        final Optional<String> message = i18n().resolve("translations", "foo.bar.unknown", ENGLISH);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenLanguageNotFound() throws Exception {
        final Optional<String> message = i18n().resolve("somebundle", "foobar", GERMAN);
        assertThat(message).isEmpty();
    }

    private static YamlI18NResolver i18n() {
        final List<Locale> supportedLocales = asList(ENGLISH, GERMAN);
        final List<String> availableBundles = asList("translations", "somebundle");
        return new YamlI18NResolver("i18n", supportedLocales, availableBundles);
    }
}
