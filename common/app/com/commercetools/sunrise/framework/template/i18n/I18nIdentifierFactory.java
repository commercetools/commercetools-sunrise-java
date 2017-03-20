package com.commercetools.sunrise.framework.template.i18n;

import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;

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

    /**
     * Given a string array and a particular position, it returns the array element in that position
     * or the default value if the array does not contain an element for that position.
     * @param array the string array from which to get the element
     * @param position the position of the element
     * @param defaultValue the default value returned in case there is no element in that position
     * @return the element from the array in that position, or the default value if there is no element
     */
    @Nullable
    private static String getArrayElement(final String[] array, final int position, final String defaultValue) {
        final boolean containsPosition = array.length > position;
        return containsPosition ? array[position] : defaultValue;
    }
}
