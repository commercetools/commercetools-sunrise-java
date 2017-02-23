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

    protected final AttributeFormatter getAttributeFormatter() {
        return attributeFormatter;
    }

    @Override
    protected ProductAttributeFormSelectableOptionViewModel newViewModelInstance(final AttributeWithProductType option, @Nullable final Attribute selectedValue) {
        return new ProductAttributeFormSelectableOptionViewModel();
    }

    @Override
    public final ProductAttributeFormSelectableOptionViewModel create(final AttributeWithProductType option, @Nullable final Attribute selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final ProductAttributeFormSelectableOptionViewModel viewModel, final AttributeWithProductType option, @Nullable final Attribute selectedValue) {
        fillLabel(viewModel, option, selectedValue);
        fillValue(viewModel, option, selectedValue);
        fillSelected(viewModel, option, selectedValue);
    }

    protected void fillLabel(final ProductAttributeFormSelectableOptionViewModel viewModel, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        viewModel.setLabel(attributeFormatter.value(option));
    }

    protected void fillValue(final ProductAttributeFormSelectableOptionViewModel viewModel, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        viewModel.setValue(attributeFormatter.valueAsKey(option));
    }

    protected void fillSelected(final ProductAttributeFormSelectableOptionViewModel viewModel, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        viewModel.setSelected(option.getAttribute().equals(selectedAttribute));
    }
}
