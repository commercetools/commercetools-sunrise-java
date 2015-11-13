package common.templates;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import play.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Objects.requireNonNull;

final class HandlebarsTranslationHelper extends Base implements Helper<String> {
    private final List<String> languages;
    private final List<String> bundles;
    private final Map<String, Map<String, Object>> languageBundleToYamlMap = new HashMap<>();

    public HandlebarsTranslationHelper(final List<String> languages, final List<String> bundles) {
        this.languages = requireNonNull(languages);
        this.bundles = requireNonNull(bundles);
        for (final String language : languages) {
            final List<String> foundBundles = new LinkedList<>();
            final List<String> notFoundBundles = new LinkedList<>();
            for (final String bundle : bundles) {
                try {
                    final Map<String, Object> yamlContent = loadYamlForTranslationAndBundle(language, bundle);
                    languageBundleToYamlMap.put(language + "/" + bundle, yamlContent);
                    foundBundles.add(bundle);
                } catch (final IOException e){
                    notFoundBundles.add(bundle);
                }
            }
            Logger.info("handlebars-i18n: {}: loaded: '{}' failed: {}", language, foundBundles, notFoundBundles);
        }
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<String> languageTags = getLocales(options);
        final String language = languageTags.get(0);//TODO improve
        final TranslationIdentifier translationIdentifier = obtainTranslationIdentifier(context);
        final String bundle = translationIdentifier.bundle;
        final String key = translationIdentifier.key;
        final String resolvedValue = pluralize(options, language, bundle, key);
        return replaceParameters(options, resolvedValue);
    }

    private String pluralize(final Options options, final String language, final String bundle, final String key) {
        final boolean containsPlural = containsPlural(options);
        return (containsPlural)
                ? Optional.ofNullable(resolve(language, bundle, key + "_plural")).orElseGet(() -> resolve(language, bundle, key))
                : resolve(language, bundle, key);
    }

    private TranslationIdentifier obtainTranslationIdentifier(final String context) {
        final String[] parts = StringUtils.split(context, ':');
        final boolean usingDefaultBundle = parts.length == 1;
        return new TranslationIdentifier(usingDefaultBundle ? "translations" : parts[0], usingDefaultBundle ? context : parts[1]);
    }

    private static class TranslationIdentifier {
        private final String key;
        private final String bundle;

        public TranslationIdentifier(final String bundle, final String key) {
            this.bundle = bundle;
            this.key = key;
        }
    }

    private String replaceParameters(final Options options, final String resolvedValue) {
        String parametersReplaced = StringUtils.defaultString(resolvedValue);
        for (final Map.Entry<String, Object> entry : options.hash.entrySet()) {
            if (entry.getValue() != null) {
                parametersReplaced = parametersReplaced.replace("__" + entry.getKey() + "__", entry.getValue().toString());
            }
        }
        return parametersReplaced;
    }

    @Nullable
    private String resolve(final String language, final String bundle, final String key) {
        final Map<String, Object> yamlContent = languageBundleToYamlMap.getOrDefault(language + "/" + bundle, Collections.emptyMap());
        return resolve(key, yamlContent);
    }

    @Nullable
    private String resolve(final String key, final Map<String, Object> yamlContent) {
        final String[] pathSegments = StringUtils.split(key, '.');
        return resolve(yamlContent, pathSegments, 0);
    }

    @Nullable
    private static String resolve(final Map<String, Object> yamlContent, final String[] pathSegments, final int index) {
        return Optional.ofNullable(yamlContent.get(pathSegments[index]))
                .map(resolved -> {
                    if (resolved instanceof String) {
                        return (String) resolved;
                    } else if (pathSegments.length == index) {
                        return null;
                    } else if (resolved instanceof Map) {
                        return resolve((Map<String, Object>) resolved, pathSegments, index + 1);
                    } else {
                        return null;
                    }
                })
                .orElse(null);
    }

    private boolean containsPlural(final Options options) {
        return options.hash.entrySet().stream()
                .anyMatch(entry -> entry.getValue() instanceof Integer && !((entry.getValue().equals(1))));
    }

    private static List<String> getLocales(final Options options) {
        return (List<String>) options.context.get("locales");
    }

    private static Map<String, Object> loadYamlForTranslationAndBundle(final String languageTag, final String bundle) throws IOException {
        final String path = buildYamlPath(languageTag, bundle);
        final InputStream inputStream = getResourceAsStream(path);
        return loadYamlData(inputStream);
    }

    private static String buildYamlPath(final String languageTag, final String bundle) {
        return "META-INF/resources/webjars/locales/" + languageTag + "/" + bundle + ".yaml";
    }

    private static Map<String, Object> loadYamlData(final InputStream inputStream) throws IOException {
        try {
            return (Map<String, Object>) new Yaml().load(inputStream);
        } catch (final YAMLException e) {
            throw new IOException(e);
        }
    }

    private static InputStream getResourceAsStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
