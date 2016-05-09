package common.template.i18n;

import io.sphere.sdk.models.Base;

import static common.utils.ArrayUtils.getArrayElement;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * i18n Identifier, consisting of the message key and the bundle.
 * @see I18nIdentifier
 */
public class I18nIdentifier extends Base {
    private static final String DEFAULT_BUNDLE = "main";
    private final String bundle;
    private final String key;

    private I18nIdentifier(final String bundle, final String key) {
        this.bundle = bundle;
        this.key = key;
    }

    public String getBundle() {
        return bundle;
    }

    public String getKey() {
        return key;
    }

    /**
     * Creates a i18n Identifier, consisting of the message key and the bundle.
     * @param bundleWithKey of the form {@code bundle:key}
     * @return the i18n Identifier for the given input
     */
    public static I18nIdentifier of(final String bundleWithKey) {
        final String[] parts = split(bundleWithKey, ":", 2);
        final String key = getArrayElement(parts, 1, bundleWithKey);
        final String bundle;
        if (bundleWithKey.equals(key)) {
            bundle = DEFAULT_BUNDLE;
        } else {
            bundle = getArrayElement(parts, 0, DEFAULT_BUNDLE);
        }
        return ofBundleAndKey(bundle, key);
    }

    public static I18nIdentifier ofBundleAndKey(final String bundle, final String key) {
        return new I18nIdentifier(bundle, key);
    }
}
