package common.models;

import common.contexts.UserContext;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;
import io.sphere.sdk.products.attributes.AttributeExtraction;
import io.sphere.sdk.producttypes.MetaProductType;

public class AttributeBean extends Base {
    private String name;
    private String key;
    private String value;

    public AttributeBean() {
    }

    public AttributeBean(final Attribute attribute, final MetaProductType metaProductType, final UserContext userContext) {
        this.name = metaProductType
                .findAttribute(attribute.getName())
                .map(def -> def.getLabel().find(userContext.locales()).orElse(""))
                .orElse(null);
        this.key = attribute.getName();
        this.value = formatValue(metaProductType, attribute, userContext);
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    protected static String formatValue(final MetaProductType metaProductType, final Attribute attribute,
                                        final UserContext userContext) {
        final AttributeExtraction<String> attributeExtraction = AttributeExtraction.of(metaProductType, attribute);
        return attributeExtraction
                .ifIs(AttributeAccess.ofLocalizedString(), v -> v.find(userContext.locales()).orElse(""))
                .ifIs(AttributeAccess.ofLocalizedEnumValue(), v -> v.getLabel().find(userContext.locales()).orElse(""))
                .ifIs(AttributeAccess.ofEnumValue(), v -> v.getLabel())
                .ifIs(AttributeAccess.ofString(), v -> v)
                .findValue()
                .orElse(null);
    }
}
