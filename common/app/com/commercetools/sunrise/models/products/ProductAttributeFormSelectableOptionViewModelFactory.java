package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.viewmodels.formatters.ProductAttributeFormatter;
import com.commercetools.sunrise.core.viewmodels.forms.SelectableViewModelFactory;
import io.sphere.sdk.products.attributes.Attribute;

import javax.annotation.Nullable;
import javax.inject.Inject;

@RequestScoped
public class ProductAttributeFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<ProductAttributeFormSelectableOptionViewModel, AttributeWithProductType, Attribute> {

    private final ProductAttributeFormatter productAttributeFormatter;

    @Inject
    public ProductAttributeFormSelectableOptionViewModelFactory(final ProductAttributeFormatter productAttributeFormatter) {
        this.productAttributeFormatter = productAttributeFormatter;
    }

    protected final ProductAttributeFormatter getProductAttributeFormatter() {
        return productAttributeFormatter;
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
        productAttributeFormatter.convert(option.getAttribute(), option.getProductTypeRef())
                .ifPresent(viewModel::setLabel);
    }

    protected void fillValue(final ProductAttributeFormSelectableOptionViewModel viewModel, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        productAttributeFormatter.convertEncoded(option.getAttribute(), option.getProductTypeRef())
                .ifPresent(viewModel::setValue);
    }

    protected void fillSelected(final ProductAttributeFormSelectableOptionViewModel viewModel, final AttributeWithProductType option, @Nullable final Attribute selectedAttribute) {
        viewModel.setSelected(option.getAttribute().equals(selectedAttribute));
    }
}
