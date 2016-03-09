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
 * Parameters and pluralized forms are supported.
 *
 * For pluralized forms, specify the amount of items as a hash parameter {@code count}.
 * For the plural message, a suffix {@code _plural} must be added to the message key.
 * Notice only pluralization forms similar to English are supported (1 is singular, the rest are plural).
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
    public Optional<String> get(final List<Locale> locales, final String bundle, final String key, final Map<String, Object> hashArgs) {
        final String message = findPluralizedTranslation(locales, bundle, key, hashArgs)
                .orElseGet(() -> findFirstTranslation(locales, bundle, key)
                        .orElse(null));
        return Optional.ofNullable(message).map(resolvedValue -> replaceParameters(resolvedValue, hashArgs));
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

    private Optional<String> findPluralizedTranslation(final List<Locale> locales, final String bundle, final String key, final Map<String, Object> hashArgs) {
        if (containsPlural(hashArgs)) {
            final String pluralizedKey = key + "_plural";
            return findFirstTranslation(locales, bundle, pluralizedKey);
        } else {
            return Optional.empty();
        }
    }

    private Optional<String> findFirstTranslation(final List<Locale> locales, final String bundle, final String key) {
        for (final Locale locale : locales) {
            final String message = resolveTranslation(locale, bundle, key);
            if (message != null) {
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

    private String replaceParameters(final String resolvedValue, final Map<String, Object> hashArgs) {
        String message = StringUtils.defaultString(resolvedValue);
        for (final Map.Entry<String, Object> entry : hashArgs.entrySet()) {
            if (entry.getValue() != null) {
                final String parameter = "__" + entry.getKey() + "__";
                message = message.replace(parameter, entry.getValue().toString());
            }
        }
        return message;
    }

    @Nullable
    private String resolveTranslation(final Locale locale, final String bundle, final String key) {
        final Map yamlContent = getYamlContent(bundle, locale);
        final String[] pathSegments = StringUtils.split(key, '.');
        return resolveTranslation(yamlContent, pathSegments, 0);
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
    private static String resolveTranslation(final Map yamlContent, final String[] pathSegments, final int index) {
        String message = null;
        if (index < pathSegments.length) {
            final Object yamlEntry = yamlContent.get(pathSegments[index]);
            if (yamlEntry instanceof String) {
                message = (String) yamlEntry;
            } else if (yamlEntry instanceof Map) {
                message = resolveTranslation((Map) yamlEntry, pathSegments, index + 1);
            }
        }
        return message;
    }

    @Nullable
    private static Map loadYamlContent(final String filepath, final Locale locale, final String bundle) throws YAMLException {
        final String path = String.format("%s/%s/%s.yaml", filepath, locale.toLanguageTag(), bundle);
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        return (Map) new Yaml().load(resourceAsStream);
    }

    private static boolean containsPlural(final Map<String, Object> hash) {
        return hash.entrySet().stream()
                .filter(entry -> entry.getKey().equals("count") && entry.getValue() instanceof Number)
                .anyMatch(entry -> ((Number) entry.getValue()).doubleValue() != 1);
    }

    private static String buildYamlKey(final String languageTag, final String bundle) {
        return languageTag + "/" + bundle;
    }
}
