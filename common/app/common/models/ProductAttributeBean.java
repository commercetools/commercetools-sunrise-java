package common.models;

import common.contexts.UserContext;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;
import io.sphere.sdk.products.attributes.AttributeExtraction;
import io.sphere.sdk.products.attributes.StringType;
import io.sphere.sdk.utils.functional.FunctionalUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class ProductAttributeBean {
    private String label;
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
        setLabel(label);
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

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
