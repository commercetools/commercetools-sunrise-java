package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.viewmodels.SimpleViewModelFactory;

import javax.inject.Inject;

@RequestScoped
public class ProductViewModelFactory extends SimpleViewModelFactory<ProductViewModel, ProductWithVariant> {

    private final ProductVariantViewModelFactory productVariantViewModelFactory;

    @Inject
    public ProductViewModelFactory(final ProductVariantViewModelFactory productVariantViewModelFactory) {
        this.productVariantViewModelFactory = productVariantViewModelFactory;
    }

    @Override
    protected ProductViewModel newViewModelInstance(final ProductWithVariant productWithVariant) {
        return new ProductViewModel();
    }

    @Override
    public final ProductViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        fillVariant(viewModel, productWithVariant);
    }

    protected void fillVariant(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setVariant(productVariantViewModelFactory.create(productWithVariant));
    }
}
