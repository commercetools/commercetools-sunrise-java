package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class SelectableProductAttributeBeanFactory extends ViewModelFactory<SelectableProductAttributeBean, SelectableProductAttributeBeanFactory.Data> {

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

    public final SelectableProductAttributeBean create(final Attribute attribute, final ProductProjection product) {
        final Data data = new Data(attribute, product);
        return initializedViewModel(data);
    }

    public final List<SelectableProductAttributeBean> createList(final ProductWithVariant productWithVariant) {
        return productDataConfig.getSelectableAttributes().stream()
                .map(productWithVariant.getVariant()::getAttribute)
                .filter(Objects::nonNull)
                .map(attribute -> create(attribute, productWithVariant.getProduct()))
                .collect(toList());
    }

    @Override
    protected SelectableProductAttributeBean getViewModelInstance() {
        return new SelectableProductAttributeBean();
    }

    @Override
    protected final void initialize(final SelectableProductAttributeBean model, final Data data) {
        fillKey(model, data);
        fillName(model, data);
        fillValue(model, data);
        fillReload(model, data);
        fillList(model, data);
        fillSelectData(model, data);
    }

    protected void fillKey(final SelectableProductAttributeBean bean, final Data data) {
        bean.setKey(data.attribute.getName());
    }

    protected void fillName(final SelectableProductAttributeBean bean, final Data data) {
        bean.setName(attributeFormatter.label(data.attribute));
    }

    protected void fillValue(final SelectableProductAttributeBean bean, final Data data) {
        bean.setValue(attributeFormatter.value(data.attribute));
    }

    protected void fillReload(final SelectableProductAttributeBean bean, final Data data) {
        bean.setReload(productDataConfig.getHardSelectableAttributes().contains(data.attribute.getName()));
    }

    protected void fillList(final SelectableProductAttributeBean bean, final Data data) {
        final List<ProductAttributeFormSelectableOptionBean> formOptions = new ArrayList<>();
        final String selectedAttributeValue = attributeFormatter.valueAsKey(data.attribute);
        data.distinctAttributeOptions.forEach(attribute ->
                formOptions.add(productAttributeFormSelectableOptionBeanFactory.create(attribute, selectedAttributeValue)));
        bean.setList(formOptions);
    }

    protected void fillSelectData(final SelectableProductAttributeBean bean, final Data data) {
        final Map<String, Map<String, List<String>>> selectableData = new HashMap<>();
        data.distinctAttributeOptions.forEach(attrOption -> {
            final String attrOptionValue = attributeFormatter.value(attrOption);
            selectableData.put(attrOptionValue, createAllowedAttributeCombinations(attrOption, data.product));
        });
        bean.setSelectData(selectableData);
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

    protected final static class Data extends Base {

        public final Attribute attribute;
        public final ProductProjection product;
        public final List<Attribute> distinctAttributeOptions;

        public Data(final Attribute attribute, final ProductProjection product) {
            this.attribute = attribute;
            this.product = product;
            this.distinctAttributeOptions = product.getAllVariants().stream()
                    .map(variant -> variant.getAttribute(attribute.getName()))
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(toList());
        }
    }
}
