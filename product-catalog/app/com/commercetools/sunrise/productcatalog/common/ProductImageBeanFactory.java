package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.Image;

import javax.inject.Singleton;

@Singleton
public class ProductImageBeanFactory extends ViewModelFactory<ProductImageBean, ProductImageBeanFactory.Data> {

    public ProductImageBeanFactory() {
    }

    public final ProductImageBean create(final Image image) {
        final Data data = new Data(image);
        return initializedViewModel(data);
    }

    @Override
    protected ProductImageBean getViewModelInstance() {
        return new ProductImageBean();
    }

    @Override
    protected final void initialize(final ProductImageBean bean, final Data data) {
        fillThumbImage(bean, data);
        fillBigImage(bean, data);
    }

    protected void fillBigImage(final ProductImageBean bean, final Data data) {
        bean.setBigImage(data.image.getUrl());
    }

    protected void fillThumbImage(final ProductImageBean bean, final Data data) {
        bean.setThumbImage(data.image.getUrl());
    }

    protected final static class Data extends Base {

        public final Image image;

        public Data(final Image image) {
            this.image = image;
        }
    }
}
