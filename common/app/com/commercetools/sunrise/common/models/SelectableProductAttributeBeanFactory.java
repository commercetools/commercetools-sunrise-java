package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class SelectableProductAttributeBeanFactory extends SelectableViewModelFactory<SelectableProductAttributeBean, List<ProductVariant>, Attribute> {

    private final AttributeFormatter attributeFormatter;
    private final ProductDataConfig productDataConfig;
    private final ProductAttributeFormSelectableOptionBeanFactory productAttributeFormSelectableOptionBeanFactory;

    @Inject
    public SelectableProductAttributeBeanFactory(final AttributeFormatter attributeFormatter, final ProductDataConfig productDataConfig,
                                                 final ProductAttributeFormSelectableOptionBeanFactory productAttributeFormSelectableOptionBeanFactory) {
        this.attributeFormatter = attributeFormatter;
        this.productDataConfig = productDataConfig;
        this.productAttributeFormSelectableOptionBeanFactory = productAttributeFormSelectableOptionBeanFactory;
    }

    @Override
    protected SelectableProductAttributeBean getViewModelInstance() {
        return new SelectableProductAttributeBean();
    }

    @Override
    public final SelectableProductAttributeBean create(final List<ProductVariant> option, final Attribute selectedValue) {
        return super.create(option, selectedValue);
    }

    public final List<SelectableProductAttributeBean> createList(final ProductWithVariant productWithVariant) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> create(productWithVariant.getProduct().getAllVariants(), attribute))
                .collect(toList());
    }

    @Override
    protected void initialize(final SelectableProductAttributeBean model, final List<ProductVariant> option, final Attribute selectedValue) {
        fillKey(model, option, selectedValue);
        fillName(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillReload(model, option, selectedValue);
        fillList(model, option, selectedValue);
        fillSelectData(model, option, selectedValue);
    }

    protected void fillKey(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final Attribute selectedAttribute) {
        model.setKey(selectedAttribute.getName());
    }

    protected void fillName(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final Attribute selectedAttribute) {
        model.setName(attributeFormatter.label(selectedAttribute));
    }

    protected void fillValue(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final Attribute selectedAttribute) {
        model.setValue(attributeFormatter.value(selectedAttribute));
    }

    protected void fillReload(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final Attribute selectedAttribute) {
        model.setReload(productDataConfig.getHardSelectableAttributes().contains(selectedAttribute.getName()));
    }

    protected void fillList(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final Attribute selectedAttribute) {
        final List<ProductAttributeFormSelectableOptionBean> formOptions = new ArrayList<>();
        final String selectedAttributeValue = attributeFormatter.valueAsKey(selectedAttribute);
        findDistinctAttributeOptions(variants, selectedAttribute).forEach(attribute ->
                formOptions.add(productAttributeFormSelectableOptionBeanFactory.create(attribute, selectedAttributeValue)));
        model.setList(formOptions);
    }

    protected void fillSelectData(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final Attribute selectedAttribute) {
        final Map<String, Map<String, List<String>>> selectableData = new HashMap<>();
        findDistinctAttributeOptions(variants, selectedAttribute).forEach(attrOption -> {
            final String attrOptionValue = attributeFormatter.value(attrOption);
            selectableData.put(attrOptionValue, createAllowedAttributeCombinations(attrOption, variants));
        });
        model.setSelectData(selectableData);
    }

    private Map<String, List<String>> createAllowedAttributeCombinations(final Attribute fixedAttribute, final List<ProductVariant> variants) {
        final Map<String, List<String>> attrCombination = new HashMap<>();
        productDataConfig.getSelectableAttributes().stream()
                .filter(enabledAttrKey -> !fixedAttribute.getName().equals(enabledAttrKey))
                .forEach(enabledAttrKey -> {
                    final List<String> allowedAttrValues = attributeCombination(enabledAttrKey, fixedAttribute, variants);
                    if (!allowedAttrValues.isEmpty()) {
                        attrCombination.put(enabledAttrKey, allowedAttrValues);
                    }
                });
        return attrCombination;
    }

    private List<String> attributeCombination(final String attributeKey, final Attribute fixedAttribute, final List<ProductVariant> variants) {
        return variants.stream()
                .filter(variant -> variant.getAttribute(attributeKey) != null)
                .filter(variant -> {
                    final Attribute variantAttribute = variant.getAttribute(fixedAttribute.getName());
                    return variantAttribute != null && variantAttribute.equals(fixedAttribute);
                })
                .map(variant -> attributeFormatter.value(variant.getAttribute(attributeKey)))
                .distinct()
                .collect(toList());
    }

    private static Stream<Attribute> findDistinctAttributeOptions(final List<ProductVariant> variants, final Attribute selectedValue) {
        return variants.stream()
                .map(variant -> variant.getAttribute(selectedValue.getName()))
                .filter(Objects::nonNull)
                .distinct();
    }
}
