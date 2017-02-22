package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.AttributeWithProductType;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.utils.AttributeFormatter;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class ProductAttributeFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<ProductAttributeFormSelectableOptionViewModel, AttributeWithProductType, Attribute> {

    private final AttributeFormatter attributeFormatter;

    @Inject
    public ProductAttributeFormSelectableOptionViewModelFactory(final AttributeFormatter attributeFormatter) {
        this.attributeFormatter = attributeFormatter;
    }

    @Override
    protected ProductAttributeFormSelectableOptionViewModel getViewModelInstance() {
        return new ProductAttributeFormSelectableOptionViewModel();
    }

    @Override
    public final ProductAttributeFormSelectableOptionViewModel create(final AttributeWithProductType option, @Nullable final Attribute selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ProductAttributeFormSelectableOptionViewModel model, final AttributeWithProductType option, @Nullable final Attribute selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final ProductAttributeFormSelectableOptionViewModel model, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        model.setLabel(attributeFormatter.value(option));
    }

    protected void fillValue(final ProductAttributeFormSelectableOptionViewModel model, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        model.setValue(attributeFormatter.valueAsKey(option));
    }

    protected void fillSelected(final ProductAttributeFormSelectableOptionViewModel model, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        model.setSelected(option.getAttribute().equals(selectedAttribute));
    }
}
