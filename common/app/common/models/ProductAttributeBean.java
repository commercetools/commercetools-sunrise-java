package common.models;

import common.contexts.UserContext;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;
import io.sphere.sdk.products.attributes.AttributeExtraction;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class ProductAttributeBean {
    private String name;
    private String key;
    private String value;

    public ProductAttributeBean() {
    }

    public ProductAttributeBean(final Attribute attr, final ProductDataConfig productDataConfig, final UserContext userContext) {
        this();
        final String label = productDataConfig.getMetaProductType()
                .findAttribute(attr.getName())
                .map(def -> def.getLabel().find(userContext.locales()).orElse(""))
                .orElse(null);
        setName(label);
        setKey(attr.getName());
        final String value = formatValue(attr, productDataConfig, userContext);
        setValue(value);
    }

    private String formatValue(final Attribute attr, final ProductDataConfig productDataConfig, final UserContext userContext) {
        return AttributeExtraction.<String>of(productDataConfig.getMetaProductType(), attr)
                    .ifIs(AttributeAccess.ofLocalizedString(), v -> v.find(userContext.locales()).orElse(""))
                    .ifIs(AttributeAccess.ofLocalizedEnumValue(), v -> v.getLabel().find(userContext.locales()).orElse(""))
                    .ifIs(AttributeAccess.ofEnumValue(), v -> v.getLabel())
                    .ifIs(AttributeAccess.ofString(), v -> v)
                    .findValue()
                    .orElse(null);
    }

    public static List<ProductAttributeBean> collect(final List<Attribute> attributes, final ProductDataConfig productDataConfig, final UserContext userContext) {
        return firstNonNull(attributes, Collections.<Attribute>emptyList()).stream()
                .filter(attr -> productDataConfig.getAttributeWhiteList().contains(attr.getName()))
                .map(attr -> new ProductAttributeBean(attr, productDataConfig, userContext))
                .collect(toList());
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
}
