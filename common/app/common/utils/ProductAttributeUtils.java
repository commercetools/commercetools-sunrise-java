package common.utils;

import common.contexts.UserContext;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;
import io.sphere.sdk.products.attributes.AttributeExtraction;
import io.sphere.sdk.producttypes.MetaProductType;

import java.util.regex.Pattern;

public final class ProductAttributeUtils {
    private static final Pattern ATTR_WHITELIST = Pattern.compile("[^0-9a-zA-Z]+");

    private ProductAttributeUtils() {
    }

    public static String attributeLabel(final Attribute attribute, final MetaProductType metaProductType,
                                        final UserContext userContext) {
        return metaProductType
                .findAttribute(attribute.getName())
                .map(def -> def.getLabel().find(userContext.locales()).orElse(""))
                .orElse("");
    }

    public static String attributeValue(final Attribute attribute, final MetaProductType metaProductType,
                                        final UserContext userContext) {
        final AttributeExtraction<String> attributeExtraction = AttributeExtraction.of(metaProductType, attribute);
        return attributeExtraction
                .ifIs(AttributeAccess.ofLocalizedString(), v -> v.find(userContext.locales()).orElse(""))
                .ifIs(AttributeAccess.ofLocalizedEnumValue(), v -> v.getLabel().find(userContext.locales()).orElse(""))
                .ifIs(AttributeAccess.ofEnumValue(), v -> v.getLabel())
                .ifIs(AttributeAccess.ofString(), v -> v)
                .findValue()
                .orElse("");
    }

    public static String attributeValueAsKey(final String attributeValue) {
        return ATTR_WHITELIST.matcher(attributeValue).replaceAll("");
    }
}
