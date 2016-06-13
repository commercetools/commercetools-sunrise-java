package com.commercetools.sunrise.common.tobedeleted;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.models.SelectableProductAttributeBean;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.attributes.Attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.commercetools.sunrise.common.utils.ProductAttributeUtils.*;
import static java.util.stream.Collectors.toList;

public class ProductAttributeBeanFactoryInjectless {

    public static ProductAttributeBean create(final Attribute attribute, final UserContext userContext, final ProductDataConfig productDataConfig) {
        final ProductAttributeBean bean = new ProductAttributeBean();
        fillAttributeInfo(bean, attribute, userContext, productDataConfig);
        return bean;
    }

    public static SelectableProductAttributeBean createSelectableAttribute(final Attribute attribute, final ProductProjection product,
                                                                           final UserContext userContext, final ProductDataConfig productDataConfig) {
        final SelectableProductAttributeBean bean = new SelectableProductAttributeBean();
        fillAttributeInfo(bean, attribute, userContext, productDataConfig);
        fillAttributeCombinations(bean, attribute, product, userContext, productDataConfig);
        bean.setReload(productDataConfig.getHardSelectableAttributes().contains(attribute.getName()));
        return bean;
    }

    private static  <T extends ProductAttributeBean> void fillAttributeInfo(final T bean, final Attribute attribute,
                                                                            final UserContext userContext, final ProductDataConfig productDataConfig) {
        bean.setKey(attribute.getName());
        bean.setName(attributeLabel(attribute, userContext.locales(), productDataConfig.getMetaProductType()));
        bean.setValue(attributeValue(attribute, userContext.locales(), productDataConfig.getMetaProductType()));
    }

    private static void fillAttributeCombinations(final SelectableProductAttributeBean bean, final Attribute attribute,
                                                  final ProductProjection product, final UserContext userContext, final ProductDataConfig productDataConfig) {
        final List<FormSelectableOptionBean> formOption = new ArrayList<>();
        final Map<String, Map<String, List<String>>> selectableData = new HashMap<>();
        product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attribute.getName()))
                .filter(attrOption -> attrOption != null)
                .distinct()
                .forEach(attrOption -> {
                    final String attrOptionValue = attributeValue(attrOption, userContext.locales(), productDataConfig.getMetaProductType());
                    formOption.add(createFormOption(attribute, attrOption, attrOptionValue));
                    selectableData.put(attrOptionValue, createAllowedAttributeCombinations(attrOption, product, userContext, productDataConfig));
                });
        bean.setList(formOption);
        bean.setSelectData(selectableData);
    }

    private static FormSelectableOptionBean createFormOption(final Attribute attribute, final Attribute attributeOption,
                                                      final String attributeOptionValue) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        bean.setLabel(attributeOptionValue);
        bean.setValue(attributeValueAsKey(attributeOptionValue));
        bean.setSelected(attributeOption.equals(attribute));
        return bean;
    }

    private static Map<String, List<String>> createAllowedAttributeCombinations(final Attribute fixedAttribute, final ProductProjection product,
                                                                                final UserContext userContext, final ProductDataConfig productDataConfig) {
        final Map<String, List<String>> attrCombination = new HashMap<>();
        productDataConfig.getSelectableAttributes().stream()
                .filter(enabledAttrKey -> !fixedAttribute.getName().equals(enabledAttrKey))
                .forEach(enabledAttrKey -> {
                    final List<String> allowedAttrValues = attributeCombination(enabledAttrKey, fixedAttribute, product, userContext, productDataConfig);
                    if (!allowedAttrValues.isEmpty()) {
                        attrCombination.put(enabledAttrKey, allowedAttrValues);
                    }
                });
        return attrCombination;
    }

    private static List<String> attributeCombination(final String attributeKey, final Attribute fixedAttribute, final ProductProjection product,
                                                     final UserContext userContext, final ProductDataConfig productDataConfig) {
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
