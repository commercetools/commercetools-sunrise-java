package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class SelectableProductAttributeBeanFactory extends SelectableViewModelFactory<SelectableProductAttributeBean, List<ProductVariant>, AttributeWithProductType> {

    private final AttributeFormatter attributeFormatter;
    private final ProductAttributeSettings productAttributeSettings;
    private final ProductAttributeFormSelectableOptionBeanFactory productAttributeFormSelectableOptionBeanFactory;

    @Inject
    public SelectableProductAttributeBeanFactory(final AttributeFormatter attributeFormatter, final ProductAttributeSettings productAttributeSettings,
                                                 final ProductAttributeFormSelectableOptionBeanFactory productAttributeFormSelectableOptionBeanFactory) {
        this.attributeFormatter = attributeFormatter;
        this.productAttributeSettings = productAttributeSettings;
        this.productAttributeFormSelectableOptionBeanFactory = productAttributeFormSelectableOptionBeanFactory;
    }

    @Override
    protected SelectableProductAttributeBean getViewModelInstance() {
        return new SelectableProductAttributeBean();
    }

    @Override
    public final SelectableProductAttributeBean create(final List<ProductVariant> option, final AttributeWithProductType selectedValue) {
        return super.create(option, selectedValue);
    }

    public final List<SelectableProductAttributeBean> createList(final ProductWithVariant productWithVariant) {
        final Reference<ProductType> productTypeRef = productWithVariant.getProduct().getProductType();
        return productAttributeSettings.getSelectableAttributes().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> create(productWithVariant.getProduct().getAllVariants(), new AttributeWithProductType(attribute, productTypeRef)))
                .collect(toList());
    }

    @Override
    protected void initialize(final SelectableProductAttributeBean model, final List<ProductVariant> option, final AttributeWithProductType selectedValue) {
        fillKey(model, option, selectedValue);
        fillName(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillReload(model, option, selectedValue);
        fillList(model, option, selectedValue);
        fillSelectData(model, option, selectedValue);
    }

    protected void fillKey(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final AttributeWithProductType selectedAttribute) {
        model.setKey(selectedAttribute.getAttribute().getName());
    }

    protected void fillName(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final AttributeWithProductType selectedAttribute) {
        model.setName(attributeFormatter.label(selectedAttribute));
    }

    protected void fillValue(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final AttributeWithProductType selectedAttribute) {
        model.setValue(attributeFormatter.value(selectedAttribute));
    }

    protected void fillReload(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final AttributeWithProductType selectedAttribute) {
        model.setReload(productAttributeSettings.getSelectablePrimaryAttributes().contains(selectedAttribute.getAttribute().getName()));
    }

    protected void fillList(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final AttributeWithProductType selectedAttribute) {
        final List<ProductAttributeFormSelectableOptionBean> formOptions = new ArrayList<>();
        findDistinctAttributeOptions(variants, selectedAttribute).forEach(attribute -> {
            final AttributeWithProductType attributeWithProductType = new AttributeWithProductType(attribute, selectedAttribute.getProductTypeRef());
            formOptions.add(productAttributeFormSelectableOptionBeanFactory.create(attributeWithProductType, selectedAttribute.getAttribute()));
        });
        model.setList(formOptions);
    }

    protected void fillSelectData(final SelectableProductAttributeBean model, final List<ProductVariant> variants, final AttributeWithProductType selectedAttribute) {
        final Map<String, Map<String, List<String>>> selectableData = new HashMap<>();
        findDistinctAttributeOptions(variants, selectedAttribute).forEach(attrOption -> {
            final AttributeWithProductType attributeOptionWithProductType = new AttributeWithProductType(attrOption, selectedAttribute.getProductTypeRef());
            final String attrOptionValue = attributeFormatter.value(attributeOptionWithProductType);
            selectableData.put(attrOptionValue, createAllowedAttributeCombinations(attributeOptionWithProductType, variants));
        });
        model.setSelectData(selectableData);
    }

    private Map<String, List<String>> createAllowedAttributeCombinations(final AttributeWithProductType fixedAttribute, final List<ProductVariant> variants) {
        final Map<String, List<String>> attrCombination = new HashMap<>();
        productAttributeSettings.getSelectableAttributes().stream()
                .filter(enabledAttrKey -> !fixedAttribute.getAttribute().getName().equals(enabledAttrKey))
                .forEach(enabledAttrKey -> {
                    final List<String> allowedAttrValues = attributeCombination(enabledAttrKey, fixedAttribute, variants);
                    if (!allowedAttrValues.isEmpty()) {
                        attrCombination.put(enabledAttrKey, allowedAttrValues);
                    }
                });
        return attrCombination;
    }

    private List<String> attributeCombination(final String attributeKey, final AttributeWithProductType fixedAttribute,
                                              final List<ProductVariant> variants) {
        return variants.stream()
                .filter(variant -> {
                    final Attribute variantAttribute = variant.getAttribute(fixedAttribute.getAttribute().getName());
                    return variantAttribute != null && variantAttribute.equals(fixedAttribute.getAttribute());
                })
                .map(variant -> variant.getAttribute(attributeKey))
                .filter(Objects::nonNull)
                .map(attribute -> attributeFormatter.value(new AttributeWithProductType(attribute, fixedAttribute.getProductTypeRef())))
                .distinct()
                .collect(toList());
    }

    private static Stream<Attribute> findDistinctAttributeOptions(final List<ProductVariant> variants, final AttributeWithProductType selectedValue) {
        return variants.stream()
                .map(variant -> variant.getAttribute(selectedValue.getAttribute().getName()))
                .filter(Objects::nonNull)
                .distinct();
    }
}
