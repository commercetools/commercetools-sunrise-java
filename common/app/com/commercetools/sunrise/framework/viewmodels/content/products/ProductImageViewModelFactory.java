package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.products.Image;

import javax.inject.Singleton;

@Singleton
public class ProductImageViewModelFactory extends SimpleViewModelFactory<ProductImageViewModel, Image> {

    @Override
    public final ProductImageViewModel create(final Image image) {
        return super.create(image);
    }

    @Override
    protected ProductImageViewModel newViewModelInstance(final Image image) {
        return new ProductImageViewModel();
    }

    @Override
    protected final void initialize(final ProductImageViewModel viewModel, final Image image) {
        fillThumbImage(viewModel, image);
        fillBigImage(viewModel, image);
    }

    protected void fillBigImage(final ProductImageViewModel viewModel, final Image image) {
        viewModel.setBigImage(image.getUrl());
    }

    protected void fillThumbImage(final ProductImageViewModel viewModel, final Image image) {
        viewModel.setThumbImage(image.getUrl());
    }
}
