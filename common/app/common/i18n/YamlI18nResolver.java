package common.i18n;

import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import play.Logger;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

/**
 * Resolves the i18n messages using YAML files defined in a webjar located in the classpath.
 */
public final class YamlI18nResolver extends Base implements I18nResolver {
    private final Map<String, Map> yamlMap = new HashMap<>();

    private YamlI18nResolver(final String filepath, final List<Locale> locales, final List<String> bundles) {
        requireNonNull(filepath);
        requireNonNull(locales);
        requireNonNull(bundles);
        for (final Locale locale : locales) {
            buildYamlMap(filepath, bundles, locale);
        }
        Logger.info("Yaml i18n resolver: Loaded {} from filepath '{}'", yamlMap.keySet(), filepath);
    }

    @Override
    public Optional<String> get(final Locale locale, final String bundle, final String key, final Object... args) {
        // TODO Work with the arguments (replace arguments, pluralize)
        final Map yamlContent = getYamlContent(bundle, locale);
        final String[] pathSegments = StringUtils.split(key, '.');
        return get(yamlContent, pathSegments, 0);
    }

    @Override
    public String toString() {
        return "YamlI18nResolver{" +
                "supportedLanguageBundles=" + yamlMap.keySet() +
                '}';
    }

    public static YamlI18nResolver of(final String filepath, final List<Locale> locales, final List<String> bundles) {
        return new YamlI18nResolver(filepath, locales, bundles);
    }

    private Optional<String> get(final Map yamlContent, final String[] pathSegments, final int index) {
        Optional<String> message = Optional.empty();
        if (index < pathSegments.length) {
            final Object yamlEntry = yamlContent.get(pathSegments[index]);
            if (yamlEntry instanceof String) {
                message = Optional.of((String) yamlEntry);
            } else if (yamlEntry instanceof Map) {
                message = get((Map) yamlEntry, pathSegments, index + 1);
            }
        }
        return message;
    }

    private void buildYamlMap(final String filepath, final List<String> bundles, final Locale locale) {
        for (final String bundle : bundles) {
            try {
                final String yamlKey = buildYamlKey(locale.toLanguageTag(), bundle);
                final Map yamlContent = loadYamlContent(filepath, locale, bundle);
                if (yamlContent != null) {
                    yamlMap.put(yamlKey, yamlContent);
                }
            } catch (final YAMLException e){
                Logger.debug("Yaml i18n resolver: Failed to load bundle '{}' for locale '{}' in filepath '{}'", bundle, locale, filepath);
            }
        }
    }

    private Map getYamlContent(final String bundle, final Locale locale) {
        final String yamlKey = buildYamlKey(locale.toLanguageTag(), bundle);
        return yamlMap.getOrDefault(yamlKey, emptyMap());
    }

    @Nullable
    private static Map loadYamlContent(final String filepath, final Locale locale, final String bundle) throws YAMLException {
        final String path = String.format("%s/%s/%s.yaml", filepath, locale.toLanguageTag(), bundle);
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        return (Map) new Yaml().load(resourceAsStream);
    }

    private static String buildYamlKey(final String languageTag, final String bundle) {
        return languageTag + "/" + bundle;
    }
}
