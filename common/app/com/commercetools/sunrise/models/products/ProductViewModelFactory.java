package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.viewmodels.SimpleViewModelFactory;

import javax.inject.Inject;
import java.util.Optional;

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
        fillProductId(viewModel, productWithVariant);
        fillVariantId(viewModel, productWithVariant);
        fillVariant(viewModel, productWithVariant);
        fillAvailability(viewModel, productWithVariant);
    }

    protected void fillProductId(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setProductId(productWithVariant.getProduct().getId());
    }

    protected void fillVariantId(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setVariantId(productWithVariant.getVariant().getId());
    }

    protected void fillVariant(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setVariant(productVariantViewModelFactory.create(productWithVariant));
    }

    protected void fillAvailability(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        Optional.ofNullable(productWithVariant.getVariant().getAvailability())
                .flatMap(productVariantAvailability -> Optional.ofNullable(productVariantAvailability.getAvailableQuantity()))
                .ifPresent(quantity -> {
                    final String status;
                    if (quantity < 4) {
                        status = "notAvailable";
                    } else if (quantity > 10) {
                        status = "available";
                    } else {
                        status = "fewItemsLeft";
                    }
                    viewModel.setAvailability(status);
                });
    }
}
