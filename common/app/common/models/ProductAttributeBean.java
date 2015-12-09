package common.models;

import common.contexts.UserContext;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.attributes.*;
import io.sphere.sdk.producttypes.MetaProductType;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class ProductAttributeBean {
    private String name;
    private String key;
    private String value;
    private List<SelectableData> list;

    public ProductAttributeBean() {
    }

    public ProductAttributeBean(final Attribute attribute, final ProductDataConfig productDataConfig,
                                final UserContext userContext) {
        this();
        final MetaProductType metaProductType = productDataConfig.getMetaProductType();
        final AttributeExtraction<String> attributeExtraction = AttributeExtraction.of(metaProductType, attribute);
        final String name = metaProductType
                .findAttribute(attribute.getName())
                .map(def -> def.getLabel().find(userContext.locales()).orElse(""))
                .orElse(null);
        setName(name);
        setKey(attribute.getName());
        final String value = formatValue(attributeExtraction, userContext);
        setValue(value);
    }

    public ProductAttributeBean(final Attribute attribute, final ProductProjection product,
                                final ProductDataConfig productDataConfig, final UserContext userContext) {
        this(attribute, productDataConfig, userContext);
        final MetaProductType metaProductType = productDataConfig.getMetaProductType();
        setList(product.getAllVariants().stream()
                .map(v -> v.getAttribute(attribute.getName()))
                .filter(v -> v != null)
                .map(a -> formatValue(AttributeExtraction.of(metaProductType, a), userContext))
                .distinct()
                .map(a -> new SelectableData(a, a))
                .collect(toList()));
    }

    private String formatValue(final AttributeExtraction<String> attributeExtraction, final UserContext userContext) {
        return attributeExtraction
                    .ifIs(AttributeAccess.ofLocalizedString(), v -> v.find(userContext.locales()).orElse(""))
                    .ifIs(AttributeAccess.ofLocalizedEnumValue(), v -> v.getLabel().find(userContext.locales()).orElse(""))
                    .ifIs(AttributeAccess.ofEnumValue(), v -> v.getLabel())
                    .ifIs(AttributeAccess.ofString(), v -> v)
                    .findValue()
                    .orElse(null);
    }

    public static List<ProductAttributeBean> collect(final ProductProjection product, final List<Attribute> attributes,
                                                     final ProductDataConfig productDataConfig, final UserContext userContext) {
        return firstNonNull(attributes, Collections.<Attribute>emptyList()).stream()
                .filter(attr -> productDataConfig.getAttributeWhiteList().contains(attr.getName()))
                .map(attr -> new ProductAttributeBean(attr, product, productDataConfig, userContext))
                .collect(toList());
    }

    public static List<ProductAttributeBean> collect(final List<Attribute> attributes, final ProductDataConfig productDataConfig,
                                                     final UserContext userContext) {
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

    public List<SelectableData> getList() {
        return list;
    }

    public void setList(final List<SelectableData> list) {
        this.list = list;
    }
}
