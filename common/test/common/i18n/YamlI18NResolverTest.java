package common.i18n;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class YamlI18nResolverTest {
    private static final YamlI18nResolver YAML_I18N_RESOLVER = yamlI18nResolver();

    @Test
    public void resolvesSimpleKey() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("default", "baz", ENGLISH);
        assertThat(message).contains("this");
    }

    @Test
    public void resolvesNestedKey() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("default", "foo.bar.qux", ENGLISH);
        assertThat(message).contains("that");
    }

    @Test
    public void resolvesSimpleKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("default", "baz", GERMAN);
        assertThat(message).contains("dies");
    }

    @Test
    public void resolvesNestedKeyInDifferentLanguage() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("default", "foo.bar.qux", GERMAN);
        assertThat(message).contains("das");
    }

    @Test
    public void resolvesSimpleKeyInDifferentBundle() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("onlyenglish", "foobar", ENGLISH);
        assertThat(message).contains("something");
    }

    @Test
    public void emptyWhenPathIsTooLong() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("default", "too.long", ENGLISH);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFound() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("default", "unknown", ENGLISH);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFoundOnNestedKey() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("default", "foo.bar.unknown", ENGLISH);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenLanguageNotFound() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("onlyenglish", "foobar", GERMAN);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenYamlFileEmpty() throws Exception {
        final Optional<String> message = YAML_I18N_RESOLVER.get("empty", "foo.bar", ENGLISH);
        assertThat(message).isEmpty();
    }

    private static YamlI18nResolver yamlI18nResolver() {
        final List<Locale> supportedLocales = asList(ENGLISH, GERMAN);
        final List<String> availableBundles = asList("default", "onlyenglish", "empty");
        return YamlI18nResolver.of("i18n", supportedLocales, availableBundles);
    }
}
