package common.template.i18n;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestableI18nResolver implements I18nResolver {
    private final Map<String, String> i18nMap;

    public TestableI18nResolver(final Map<String, String> i18nMap) {
        this.i18nMap = i18nMap;
    }

    @Override
    public Optional<String> get(final List<Locale> locales, final I18nIdentifier i18nIdentifier, final Map<String, Object> hashArgs) {
        final String mapKey = String.format("%s/%s:%s", locales.get(0), i18nIdentifier.getBundle(), i18nIdentifier.getMessageKey());
        final String message = i18nMap.get(mapKey);
        final String parameters = hashArgs.entrySet().stream()
                .map(hashPair -> hashPair.getKey() + "=" + hashPair.getValue())
                .collect(Collectors.joining(","));
        if (parameters.isEmpty()) {
            return Optional.ofNullable(message);
        } else {
            return Optional.of(message + " " + parameters);
        }
    }
}
