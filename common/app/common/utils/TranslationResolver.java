package common.utils;

import io.sphere.sdk.models.LocalizedString;

@FunctionalInterface
public interface TranslationResolver {
    String getTranslation(LocalizedString localizedString);
}
