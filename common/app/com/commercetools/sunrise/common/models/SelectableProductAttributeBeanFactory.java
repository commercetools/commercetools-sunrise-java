package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class SelectableProductAttributeBeanFactory extends ViewModelFactory {

    private final AttributeFormatter attributeFormatter;
    private final ProductDataConfig productDataConfig;

    @Inject
    public SelectableProductAttributeBeanFactory(final AttributeFormatter attributeFormatter, final ProductDataConfig productDataConfig) {
        this.attributeFormatter = attributeFormatter;
        this.productDataConfig = productDataConfig;
    }

    public SelectableProductAttributeBean create(final Attribute attribute, final ProductProjection product) {
        final SelectableProductAttributeBean bean = new SelectableProductAttributeBean();
        initialize(bean, attribute, product);
        return bean;
    }

    public List<SelectableProductAttributeBean> createList(final ProductVariant variant, final ProductProjection product) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .map(attr -> create(attr, product))
                .collect(toList());
    }

    protected final void initialize(final SelectableProductAttributeBean bean, final Attribute attribute, final ProductProjection product) {
        fillAttributeInfo(bean, attribute);
        fillReload(bean, attribute);
        fillAttributeCombinations(bean, attribute, product);
    }

    protected void fillAttributeInfo(final SelectableProductAttributeBean bean, final Attribute attribute) {
        bean.setKey(attribute.getName());
        bean.setName(attributeFormatter.label(attribute));
        bean.setValue(attributeFormatter.value(attribute));
    }

    protected void fillReload(final SelectableProductAttributeBean bean, final Attribute attribute) {
        bean.setReload(productDataConfig.getHardSelectableAttributes().contains(attribute.getName()));
    }

    protected void fillAttributeCombinations(final SelectableProductAttributeBean bean, final Attribute attribute, final ProductProjection product) {
        final Map<String, Map<String, List<String>>> selectableData = new HashMap<>();
        final List<FormSelectableOptionBean> formOptions = new ArrayList<>();
        product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attribute.getName()))
                .filter(Objects::nonNull)
                .distinct()
                .forEach(attrOption -> {
                    final String attrOptionValue = attributeFormatter.value(attrOption);
                    formOptions.add(createFormOption(attribute, attrOption, attrOptionValue));
                    selectableData.put(attrOptionValue, createAllowedAttributeCombinations(attrOption, product));
                });
        bean.setList(formOptions);
        bean.setSelectData(selectableData);
    }

    private FormSelectableOptionBean createFormOption(final Attribute attribute, final Attribute attributeOption,
                                                      final String attributeOptionValue) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        bean.setLabel(attributeOptionValue);
        bean.setValue(attributeFormatter.valueAsKey(attribute));
        bean.setSelected(attributeOption.equals(attribute));
        return bean;
    }

    private Map<String, List<String>> createAllowedAttributeCombinations(final Attribute fixedAttribute, final ProductProjection product) {
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

    private List<String> attributeCombination(final String attributeKey, final Attribute fixedAttribute, final ProductProjection product) {
        return product.getAllVariants().stream()
                .filter(variant -> variant.getAttribute(attributeKey) != null)
                .filter(variant -> {
                    final Attribute variantAttribute = variant.getAttribute(fixedAttribute.getName());
                    return variantAttribute != null && variantAttribute.equals(fixedAttribute);
                })
                .map(variant -> attributeFormatter.value(variant.getAttribute(attributeKey)))
                .distinct()
                .collect(toList());
    }
}
