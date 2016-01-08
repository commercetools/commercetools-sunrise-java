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
    private static final YamlI18nResolver YAML_I18N_RESOLVER = yamlI18nResolver();

    @Test
    public void resolvesSimpleKey() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(ENGLISH, "default", "baz");
        assertThat(message).contains("this");
    }

    @Test
    public void resolvesNestedKey() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(ENGLISH, "default", "foo.bar.qux");
        assertThat(message).contains("that");
    }

    @Test
    public void resolvesSimpleKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(GERMAN, "default", "baz");
        assertThat(message).contains("dies");
    }

    @Test
    public void resolvesNestedKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(GERMAN, "default", "foo.bar.qux");
        assertThat(message).contains("das");
    }

    @Test
    public void resolvesSimpleKeyInDifferentBundle() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(ENGLISH, "onlyenglish", "foobar");
        assertThat(message).contains("something");
    }

    @Test
    public void emptyWhenPathIsTooLong() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(ENGLISH, "default", "too.long");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFound() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(ENGLISH, "default", "unknown");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFoundOnNestedKey() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(ENGLISH, "default", "foo.bar.unknown");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenLanguageNotFound() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(GERMAN, "onlyenglish", "foobar");
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenYamlFileEmpty() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get(ENGLISH, "empty", "foo.bar");
        assertThat(message).isEmpty();
    }

    private static YamlI18nResolver yamlI18nResolver() {
        final List<Locale> supportedLocales = asList(ENGLISH, GERMAN);
        final List<String> availableBundles = asList("default", "onlyenglish", "empty");
        return YamlI18nResolver.of("i18n", supportedLocales, availableBundles);
    }
}
