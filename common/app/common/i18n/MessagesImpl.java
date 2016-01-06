package common.i18n;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.error.YAMLException;
import play.Logger;
import play.libs.Yaml;

import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

public class MessagesImpl implements Messages {
    private static final String I18N_FILEPATH = "META-INF/resources/webjars/locales";
    private final Map<String, Map> yamlMap = new HashMap<>();

    public MessagesImpl(final List<Locale> locales, final List<String> bundles) {
        requireNonNull(locales);
        requireNonNull(bundles);
        for (final Locale locale : locales) {
            buildYamlMap(bundles, locale);
        }
        Logger.debug("i18n - Loaded {}", yamlMap.keySet());
    }

    private void buildYamlMap(final List<String> bundles, final Locale locale) {
        for (final String bundle : bundles) {
            try {
                final String yamlKey = buildYamlKey(locale.toLanguageTag(), bundle);
                final Map yamlContent = loadYamlContent(locale, bundle);
                yamlMap.put(yamlKey, yamlContent);
            } catch (final YAMLException e){
                Logger.error("i18n - Failed to load bundle '{}' for locale '{}'", bundle, locale);
            }
        }
    }

    @Override
    public Optional<String> get(final String bundle, final String key, final Locale locale) {
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

    private Map getYamlContent(final String bundle, final Locale locale) {
        final String yamlKey = buildYamlKey(locale.toLanguageTag(), bundle);
        return yamlMap.getOrDefault(yamlKey, emptyMap());
    }

    private static Map loadYamlContent(final Locale locale, final String bundle) throws YAMLException {
        final String path = obtainYamlPath(locale, bundle);
        return (Map) Yaml.load(path);
    }

    private static String obtainYamlPath(final Locale locale, final String bundle) {
        return String.format("%s/%s/%s.yaml", I18N_FILEPATH, locale.toLanguageTag(), bundle);
    }

    private static String buildYamlKey(final String languageTag, final String bundle) {
        return languageTag + "/" + bundle;
    }

    @Override
    public String toString() {
        return "I18nUtils{" +
                "supportedLanguages=" + yamlMap.keySet() +
                '}';
    }
}
