package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.Image;

import javax.inject.Singleton;

@Singleton
public class ProductImageViewModelFactory extends ViewModelFactory<ProductImageViewModel, Image> {

    public ProductImageViewModelFactory() {
    }

    @Override
    public final ProductImageViewModel create(final Image image) {
        return super.create(image);
    }

    @Override
    protected ProductImageViewModel getViewModelInstance(final Image image) {
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
