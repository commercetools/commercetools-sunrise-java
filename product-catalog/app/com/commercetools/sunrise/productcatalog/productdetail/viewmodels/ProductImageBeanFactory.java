package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.Image;

import javax.inject.Singleton;

@Singleton
public class ProductImageBeanFactory extends ViewModelFactory<ProductImageBean, Image> {

    public ProductImageBeanFactory() {
    }

    @Override
    public final ProductImageBean create(final Image data) {
        return super.create(data);
    }

    @Override
    protected ProductImageBean getViewModelInstance() {
        return new ProductImageBean();
    }

    @Override
    protected final void initialize(final ProductImageBean model, final Image image) {
        fillThumbImage(model, image);
        fillBigImage(model, image);
    }

    protected void fillBigImage(final ProductImageBean bean, final Image image) {
        bean.setBigImage(image.getUrl());
    }

    protected void fillThumbImage(final ProductImageBean bean, final Image image) {
        bean.setThumbImage(image.getUrl());
    }
}
