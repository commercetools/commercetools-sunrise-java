package common.templates;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class HandlebarsTranslationHelper extends Base implements Helper<String> {

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<String> languageTags = getLocales(options);
        final String languageTag = languageTags.get(0);//TODO improve

        final String[] parts = StringUtils.split(context, ':');
        final boolean usingDefaultBundle = parts.length == 1;
        final String bundle = usingDefaultBundle ? "translations" : parts[0];
        final String key = usingDefaultBundle ? context : parts[1];
        final Map<String, Object> yamlContent = loadYamlForTranslationAndBundle(languageTag, bundle);
        final String[] pathSegments = StringUtils.split(key, '.');
        final String resolvedValue = resolve(yamlContent, pathSegments, 0);

        String parametersReplaced = resolvedValue;
        for (final Map.Entry<String, Object> entry : options.hash.entrySet()) {
            parametersReplaced = parametersReplaced.replace("__" + entry.getKey() + "__", entry.getValue().toString());
        }

        return parametersReplaced;
    }

    @Nullable
    private static String resolve(final Map<String, Object> yamlContent, final String[] pathSegments, final int index) {
        return Optional.ofNullable(yamlContent.get(pathSegments[index]))
                .map(resolved -> {
                    if (/*pathSegments.length == index - 1 && TODO */resolved instanceof String) {
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

    private static List<String> getLocales(final Options options) {
        return (List<String>) options.context.get("locales");
    }

    private static Map<String, Object> loadYamlForTranslationAndBundle(final String languageTag, final String bundle) {
        final String path = buildYamlPath(languageTag, bundle);
        final InputStream inputStream = getResourceAsStream(path);
        return loadYamlData(inputStream);
    }

    private static String buildYamlPath(final String languageTag, final String bundle) {
        return "META-INF/resources/webjars/locales/" + languageTag + "/" + bundle + ".yaml";
    }

    private static Map<String, Object> loadYamlData(final InputStream inputStream) {
        return (Map<String, Object>) new Yaml().load(inputStream);
    }

    private static InputStream getResourceAsStream(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
