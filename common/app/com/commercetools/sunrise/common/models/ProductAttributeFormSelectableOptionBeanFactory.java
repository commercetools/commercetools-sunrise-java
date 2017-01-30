package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class ProductAttributeFormSelectableOptionBeanFactory extends SelectableViewModelFactory<ProductAttributeFormSelectableOptionBean, Attribute, String> {

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
    public final ProductAttributeFormSelectableOptionBean create(final Attribute option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ProductAttributeFormSelectableOptionBean model, final Attribute option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final ProductAttributeFormSelectableOptionBean model, final Attribute option, @Nullable final String selectedValue) {
        model.setLabel(attributeFormatter.value(option));
    }

    protected void fillValue(final ProductAttributeFormSelectableOptionBean model, final Attribute option, @Nullable final String selectedValue) {
        model.setValue(attributeFormatter.valueAsKey(option));
    }

    protected void fillSelected(final ProductAttributeFormSelectableOptionBean model, final Attribute option, @Nullable final String selectedValue) {
        model.setSelected(attributeFormatter.valueAsKey(option).equals(selectedValue));
    }
}
