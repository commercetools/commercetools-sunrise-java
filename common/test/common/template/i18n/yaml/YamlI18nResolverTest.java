package common.template.i18n.yaml;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.yaml.YamlI18nResolver;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;

public class YamlI18nResolverTest {
    private static final YamlI18nResolver YAML_I18N_RESOLVER = yamlI18nResolver();

    @Test
    public void resolvesSimpleKey() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("default", "baz");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).contains("this");
    }

    @Test
    public void resolvesNestedKey() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("default", "foo.bar.qux");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).contains("that");
    }

    @Test
    public void resolvesSimpleKeyInDifferentLanguage() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("default", "baz");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(GERMAN), i18nIdentifier);
        assertThat(message).contains("dies");
    }

    @Test
    public void resolvesNestedKeyInDifferentLanguage() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("default", "foo.bar.qux");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(GERMAN), i18nIdentifier);
        assertThat(message).contains("das");
    }

    @Test
    public void resolvesSimpleKeyInDifferentBundle() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("onlyenglish", "foobar");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).contains("something");
    }

    @Test
    public void resolvesLongSinglePath() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("singlelong", "foo.bar.title");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).contains("correct");
    }

    @Test
    public void emptyWhenKeyIsNotALeaf() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("default", "too.long");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFound() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("default", "unknown");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenKeyNotFoundOnNestedKey() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("default", "foo.bar.unknown");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenLanguageNotFound() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("onlyenglish", "foobar");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(GERMAN), i18nIdentifier);
        assertThat(message).isEmpty();
    }

    @Test
    public void emptyWhenYamlFileEmpty() throws Exception {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("empty", "foo.bar");
        final Optional<String> message = YAML_I18N_RESOLVER.get(singletonList(ENGLISH), i18nIdentifier);
        assertThat(message).isEmpty();
    }

    @Test
    public void resolvesParameter() throws Exception {
        final Map<String, Object> hash = new HashMap<>();
        hash.put("title", "Mr.");
        hash.put("surname", "Doe");
        hash.put("name", "John Doe");
        assertThat(resolveWithParameters(ENGLISH, "greetings", hash)).contains("Hello John Doe!");
        assertThat(resolveWithParameters(ENGLISH, "formalgreetings", hash)).contains("Dear Mr. Doe");
    }

    @Test
    public void resolvesPlural() throws Exception {
        assertThat(pluralizedFormOf(ENGLISH, "items", 0)).contains("0 items");
        assertThat(pluralizedFormOf(ENGLISH, "items", 1)).contains("1 item");
        assertThat(pluralizedFormOf(ENGLISH, "items", 2)).contains("2 items");
        assertThat(pluralizedFormOf(ENGLISH, "items", 10)).contains("10 items");
    }

    @Test
    public void resolvesWhenPluralNotDefined() throws Exception {
        assertThat(pluralizedFormOf(ENGLISH, "withoutplural", 0)).contains("0 item without plural");
        assertThat(pluralizedFormOf(ENGLISH, "withoutplural", 1)).contains("1 item without plural");
        assertThat(pluralizedFormOf(ENGLISH, "withoutplural", 2)).contains("2 item without plural");
        assertThat(pluralizedFormOf(ENGLISH, "withoutplural", 10)).contains("10 item without plural");
    }

    private static Optional<String> pluralizedFormOf(final Locale locale, final String key, final int count) {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("pluralized", key);
        return YAML_I18N_RESOLVER.get(singletonList(locale), i18nIdentifier, singletonMap("count", count));
    }

    private static Optional<String> resolveWithParameters(final Locale locale, final String key, final Map<String, Object> hash) {
        final I18nIdentifier i18nIdentifier = I18nIdentifier.ofBundleAndKey("parameters", key);
        return YAML_I18N_RESOLVER.get(singletonList(locale), i18nIdentifier, hash);
    }

    private static YamlI18nResolver yamlI18nResolver() {
        final List<Locale> supportedLocales = asList(ENGLISH, GERMAN);
        final List<String> availableBundles = asList("default", "onlyenglish", "empty", "singlelong", "parameters", "pluralized");
        return YamlI18nResolver.of("i18n", supportedLocales, availableBundles);
    }
}
