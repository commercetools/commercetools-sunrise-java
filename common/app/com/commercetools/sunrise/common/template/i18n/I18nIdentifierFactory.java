package com.commercetools.sunrise.common.template.i18n;

import io.sphere.sdk.models.Base;

import static com.commercetools.sunrise.common.utils.ArrayUtils.getArrayElement;
import static org.apache.commons.lang3.StringUtils.split;

public class I18nIdentifierFactory extends Base {

    private static final String DEFAULT_BUNDLE = "main";

    public I18nIdentifier create(final String bundle, final String messageKey) {
        return I18nIdentifier.of(bundle, messageKey);
    }

    /**
     * Creates a i18n Identifier, consisting of the bundle and the message key.
     * @param bundleWithMessageKey of the form {@code bundle:key}
     * @return the i18n Identifier for the given input
     */
    public I18nIdentifier create(final String bundleWithMessageKey) {
        final String[] parts = split(bundleWithMessageKey, ":", 2);
        final String key = getArrayElement(parts, 1, bundleWithMessageKey);
        final String bundle;
        if (bundleWithMessageKey.equals(key)) {
            bundle = getDefaultBundle();
        } else {
            bundle = getArrayElement(parts, 0, getDefaultBundle());
        }
        return I18nIdentifier.of(bundle, key);
    }

    protected String getDefaultBundle() {
        return DEFAULT_BUNDLE;
    }
}
