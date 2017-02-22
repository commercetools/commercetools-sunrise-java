package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.Image;

import javax.inject.Singleton;

@Singleton
public class ProductImageViewModelFactory extends ViewModelFactory<ProductImageViewModel, Image> {

    public ProductImageViewModelFactory() {
    }

    @Override
    public final ProductImageViewModel create(final Image data) {
        return super.create(data);
    }

    @Override
    protected ProductImageViewModel getViewModelInstance() {
        return new ProductImageViewModel();
    }

    @Override
    protected final void initialize(final ProductImageViewModel model, final Image image) {
        fillThumbImage(model, image);
        fillBigImage(model, image);
    }

    protected void fillBigImage(final ProductImageViewModel model, final Image image) {
        model.setBigImage(image.getUrl());
    }

    protected void fillThumbImage(final ProductImageViewModel model, final Image image) {
        model.setThumbImage(image.getUrl());
    }
}
