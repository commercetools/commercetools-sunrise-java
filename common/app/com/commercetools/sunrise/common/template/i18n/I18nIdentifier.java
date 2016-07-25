package com.commercetools.sunrise.common.template.i18n;

import io.sphere.sdk.models.Base;

import static com.commercetools.sunrise.common.utils.ArrayUtils.getArrayElement;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * i18n Identifier, consisting of the message key and the bundle.
 *
 * - {@code bundle}: usually represents a module (e.g. catalog)
 * - {@code messageKey}: can contain multiple path elements (e.g. pagination.next)
 *
 * @see I18nIdentifier
 */
public final class I18nIdentifier extends Base {

    private final String bundle;
    private final String messageKey;

    private I18nIdentifier(final String bundle, final String messageKey) {
        this.bundle = bundle;
        this.messageKey = messageKey;
    }

    public String bundle() {
        return bundle;
    }

    public String messageKey() {
        return messageKey;
    }

    public static I18nIdentifier of(final String bundle, final String key) {
        return new I18nIdentifier(bundle, key);
    }

    /**
     * Creates a i18n Identifier, consisting of the message key and the bundle.
     * @param bundleWithKey of the form {@code bundle:key}
     * @return the i18n Identifier for the given input
     * @deprecated use {@link I18nIdentifierFactory} instead
     */
    @Deprecated
    public static I18nIdentifier of(final String bundleWithKey) {
        final String[] parts = split(bundleWithKey, ":", 2);
        final String key = getArrayElement(parts, 1, bundleWithKey);
        final String bundle;
        if (bundleWithKey.equals(key)) {
            bundle = "main";
        } else {
            bundle = getArrayElement(parts, 0, "main");
        }
        return ofBundleAndKey(bundle, key);
    }

    /**
     * @deprecated use {@link #of} instead
     */
    @Deprecated
    public static I18nIdentifier ofBundleAndKey(final String bundle, final String key) {
        return of(bundle, key);
    }
}
