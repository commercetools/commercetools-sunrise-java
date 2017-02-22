package com.commercetools.sunrise.common.models.products;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductVariant;

import java.util.Optional;

public abstract class AbstractProductVariantViewModelFactory<D> extends ViewModelFactory<ProductVariantViewModel, D> {

    protected AbstractProductVariantViewModelFactory() {
    }

    @Override
    protected ProductVariantViewModel getViewModelInstance() {
        return new ProductVariantViewModel();
    }

    @Override
    protected void initialize(final ProductVariantViewModel viewModel, final D input) {
        fillSku(viewModel, input);
        fillName(viewModel, input);
        fillUrl(viewModel, input);
        fillImage(viewModel, input);
        fillPrice(viewModel, input);
        fillPriceOld(viewModel, input);
    }

    protected abstract void fillSku(final ProductVariantViewModel model, final D data);

    protected abstract void fillName(final ProductVariantViewModel model, final D data);

    protected abstract void fillUrl(final ProductVariantViewModel model, final D data);

    protected abstract void fillImage(final ProductVariantViewModel model, final D data);

    protected abstract void fillPrice(final ProductVariantViewModel model, final D data);

    protected abstract void fillPriceOld(final ProductVariantViewModel model, final D data);

    protected String findSku(final ProductVariant variant) {
        return variant.getSku();
    }

    protected Optional<String> findImageUrl(final ProductVariant variant) {
        return variant.getImages().stream()
                .findFirst()
                .map(Image::getUrl);
    }
}