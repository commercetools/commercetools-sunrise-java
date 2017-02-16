package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class ProductAttributeFormSelectableOptionBeanFactory extends SelectableViewModelFactory<ProductAttributeFormSelectableOptionBean, AttributeWithProductType, Attribute> {

    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductAttributeFormSelectableOptionBeanFactory(final AttributeFormatter attributeFormatter) {
        this.attributeFormatter = attributeFormatter;
    }

    @Override
    protected ProductAttributeFormSelectableOptionBean getViewModelInstance() {
        return new ProductAttributeFormSelectableOptionBean();
    }

    @Override
    public final ProductAttributeFormSelectableOptionBean create(final AttributeWithProductType option, @Nullable final Attribute selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ProductAttributeFormSelectableOptionBean model, final AttributeWithProductType option, @Nullable final Attribute selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final ProductAttributeFormSelectableOptionBean model, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        model.setLabel(attributeFormatter.value(option));
    }

    protected void fillValue(final ProductAttributeFormSelectableOptionBean model, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        model.setValue(attributeFormatter.valueAsKey(option));
    }

    protected void fillSelected(final ProductAttributeFormSelectableOptionBean model, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        model.setSelected(option.getAttribute().equals(selectedAttribute));
    }
}
