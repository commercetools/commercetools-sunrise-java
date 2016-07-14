package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.commercetools.sunrise.common.utils.ProductAttributeUtils.*;
import static java.util.stream.Collectors.toList;

public class ProductAttributeBeanFactory extends Base {

    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    protected UserContext userContext;

    public ProductAttributeBean create(final Attribute attribute) {
        return fillBean(new ProductAttributeBean(), attribute);
    }

    protected <T extends ProductAttributeBean> T fillBean(final T bean, final Attribute attribute) {
        fillAttributeInfo(bean, attribute);
        return bean;
    }

    public SelectableProductAttributeBean createSelectableAttribute(final Attribute attribute, final ProductProjection product) {
        final SelectableProductAttributeBean bean = new SelectableProductAttributeBean();
        fillAttributeInfo(bean, attribute);
        fillAttributeCombinations(bean, attribute, product);
        fillReload(attribute, bean);
        return bean;
    }

    protected void fillReload(final Attribute attribute, final SelectableProductAttributeBean bean) {
        bean.setReload(productDataConfig.getHardSelectableAttributes().contains(attribute.getName()));
    }

    protected <T extends ProductAttributeBean> void fillAttributeInfo(final T bean, final Attribute attribute) {
        bean.setKey(attribute.getName());
        bean.setName(attributeLabel(attribute, userContext.locales(), productDataConfig.getMetaProductType()));
        bean.setValue(attributeValue(attribute, userContext.locales(), productDataConfig.getMetaProductType()));
    }

    protected void fillAttributeCombinations(final SelectableProductAttributeBean bean, final Attribute attribute,
                                           final ProductProjection product) {
        final List<FormSelectableOptionBean> formOption = new ArrayList<>();
        final Map<String, Map<String, List<String>>> selectableData = new HashMap<>();
        product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attribute.getName()))
                .filter(attrOption -> attrOption != null)
                .distinct()
                .forEach(attrOption -> {
                    final String attrOptionValue = attributeValue(attrOption, userContext.locales(), productDataConfig.getMetaProductType());
                    formOption.add(createFormOption(attribute, attrOption, attrOptionValue));
                    selectableData.put(attrOptionValue, createAllowedAttributeCombinations(attrOption, product));
                });
        bean.setList(formOption);
        bean.setSelectData(selectableData);
    }

    protected FormSelectableOptionBean createFormOption(final Attribute attribute, final Attribute attributeOption,
                                                      final String attributeOptionValue) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        bean.setLabel(attributeOptionValue);
        bean.setValue(attributeValueAsKey(attributeOptionValue));
        bean.setSelected(attributeOption.equals(attribute));
        return bean;
    }

    protected Map<String, List<String>> createAllowedAttributeCombinations(final Attribute fixedAttribute, final ProductProjection product) {
        final Map<String, List<String>> attrCombination = new HashMap<>();
        productDataConfig.getSelectableAttributes().stream()
                .filter(enabledAttrKey -> !fixedAttribute.getName().equals(enabledAttrKey))
                .forEach(enabledAttrKey -> {
                    final List<String> allowedAttrValues = attributeCombination(enabledAttrKey, fixedAttribute, product);
                    if (!allowedAttrValues.isEmpty()) {
                        attrCombination.put(enabledAttrKey, allowedAttrValues);
                    }
                });
        return attrCombination;
    }

    protected List<String> attributeCombination(final String attributeKey, final Attribute fixedAttribute, final ProductProjection product) {
        return product.getAllVariants().stream()
                .filter(variant -> variant.getAttribute(attributeKey) != null)
                .filter(variant -> {
                    final Attribute variantAttribute = variant.getAttribute(fixedAttribute.getName());
                    return variantAttribute != null && variantAttribute.equals(fixedAttribute);
                })
                .map(variant -> attributeValue(variant.getAttribute(attributeKey), userContext.locales(), productDataConfig.getMetaProductType()))
                .distinct()
                .collect(toList());
    }
}
