package common.utils;

import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;
import io.sphere.sdk.products.attributes.AttributeExtraction;
import io.sphere.sdk.producttypes.MetaProductType;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class ProductAttributeUtils {

    private static final Pattern ATTR_WHITELIST = Pattern.compile("[^0-9a-zA-Z]+");

    private ProductAttributeUtils() {
    }

    public static String attributeLabel(final Attribute attribute, final List<Locale> locales, final MetaProductType metaProductType) {
        return metaProductType
                .findAttribute(attribute.getName())
                .map(def -> def.getLabel().find(locales).orElse(""))
                .orElse("");
    }

    public static String attributeValue(final Attribute attribute, final List<Locale> locales, final MetaProductType metaProductType) {
        final AttributeExtraction<String> attributeExtraction = AttributeExtraction.of(metaProductType, attribute);
        return attributeExtraction
                .ifIs(AttributeAccess.ofLocalizedString(), v -> v.find(locales).orElse(""))
                .ifIs(AttributeAccess.ofLocalizedEnumValue(), v -> v.getLabel().find(locales).orElse(""))
                .ifIs(AttributeAccess.ofEnumValue(), v -> v.getLabel())
                .ifIs(AttributeAccess.ofString(), v -> v)
                .findValue()
                .orElse("");
    }

    public static String attributeValueAsKey(final String attributeValue) {
        return ATTR_WHITELIST.matcher(attributeValue).replaceAll("");
    }
}
