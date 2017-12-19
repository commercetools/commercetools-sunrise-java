package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.core.viewmodels.formatters.ProductAttributeFormatter;

import javax.inject.Inject;

@RequestScoped
public class ProductAttributeViewModelFactory extends SimpleViewModelFactory<ProductAttributeViewModel, AttributeWithProductType> {

    private final ProductAttributeFormatter productAttributeFormatter;

    @Inject
    public ProductAttributeViewModelFactory(final ProductAttributeFormatter productAttributeFormatter) {
        this.productAttributeFormatter = productAttributeFormatter;
    }

    protected final ProductAttributeFormatter getProductAttributeFormatter() {
        return productAttributeFormatter;
    }

    @Override
    protected ProductAttributeViewModel newViewModelInstance(final AttributeWithProductType attributeWithProductType) {
        return new ProductAttributeViewModel();
    }

    @Override
    public final ProductAttributeViewModel create(final AttributeWithProductType attributeWithProductType) {
        return super.create(attributeWithProductType);
    }

    @Override
    protected final void initialize(final ProductAttributeViewModel viewModel, final AttributeWithProductType attributeWithProductType) {
        fillKey(viewModel, attributeWithProductType);
        fillName(viewModel, attributeWithProductType);
        fillValue(viewModel, attributeWithProductType);
    }

    protected void fillKey(final ProductAttributeViewModel viewModel, final AttributeWithProductType attributeWithProductType) {
        viewModel.setKey(attributeWithProductType.getAttribute().getName());
    }

    protected void fillName(final ProductAttributeViewModel viewModel, final AttributeWithProductType attributeWithProductType) {
        productAttributeFormatter.label(attributeWithProductType.getAttribute().getName(), attributeWithProductType.getProductTypeRef())
                .ifPresent(viewModel::setName);
    }

    protected void fillValue(final ProductAttributeViewModel viewModel, final AttributeWithProductType attributeWithProductType) {
        productAttributeFormatter.convert(attributeWithProductType.getAttribute(), attributeWithProductType.getProductTypeRef())
                .ifPresent(viewModel::setValue);
    }
}
