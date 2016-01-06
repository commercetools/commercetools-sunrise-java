package common.i18n;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import play.Logger;

import java.io.InputStream;
import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

/**
 * Resolves the i18n messages using YAML files defined in a webjar located in the classpath.
 */
final class YamlI18NResolver implements I18nResolver {
    private final Map<String, Map> yamlMap = new HashMap<>();

    YamlI18NResolver(final String filepath, final List<Locale> locales, final List<String> bundles) {
        requireNonNull(filepath);
        requireNonNull(locales);
        requireNonNull(bundles);
        for (final Locale locale : locales) {
            buildYamlMap(filepath, bundles, locale);
        }
        Logger.debug("i18n - Loaded {}", yamlMap.keySet());
    }

    @Override
    public Optional<String> resolve(final String bundle, final String key, final Locale locale) {
        final Map yamlContent = getYamlContent(bundle, locale);
        final String[] pathSegments = StringUtils.split(key, '.');
        return resolve(yamlContent, pathSegments, 0);
    }

    private Optional<String> resolve(final Map yamlContent, final String[] pathSegments, final int index) {
        final Object yamlEntry = yamlContent.get(pathSegments[index]);
        final Optional<String> message;
        if (yamlEntry instanceof String) {
            message = Optional.of((String) yamlEntry);
        } else if (yamlEntry instanceof Map && index < pathSegments.length) {
            message = resolve((Map) yamlEntry, pathSegments, index + 1);
        } else {
            message = Optional.empty();
        }
        return message;
    }

    private void buildYamlMap(final String filepath, final List<String> bundles, final Locale locale) {
        for (final String bundle : bundles) {
            try {
                final String yamlKey = buildYamlKey(locale.toLanguageTag(), bundle);
                final Map yamlContent = loadYamlContent(filepath, locale, bundle);
                yamlMap.put(yamlKey, yamlContent);
            } catch (final YAMLException e){
                Logger.error("i18n - Failed to load bundle '{}' for locale '{}'", bundle, locale);
            }
        }
    }

    private Map getYamlContent(final String bundle, final Locale locale) {
        final String yamlKey = buildYamlKey(locale.toLanguageTag(), bundle);
        return yamlMap.getOrDefault(yamlKey, emptyMap());
    }

    private static Map loadYamlContent(final String filepath, final Locale locale, final String bundle) throws YAMLException {
        final String path = String.format("%s/%s/%s.yaml", filepath, locale.toLanguageTag(), bundle);
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        return (Map) new Yaml().load(resourceAsStream);
    }

    private static String buildYamlKey(final String languageTag, final String bundle) {
        return languageTag + "/" + bundle;
    }

    @Override
    public String toString() {
        return "I18nUtils{" +
                "supportedLanguageBundles=" + yamlMap.keySet() +
                '}';
    }
}
