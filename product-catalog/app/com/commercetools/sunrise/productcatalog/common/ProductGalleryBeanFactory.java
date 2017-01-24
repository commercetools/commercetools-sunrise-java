package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.stream.Collectors.toList;

@Singleton
public class ProductGalleryBeanFactory extends ViewModelFactory<ProductGalleryBean, ProductGalleryBeanFactory.Data> {

    private final ProductImageBeanFactory productImageBeanFactory;

    @Inject
    public ProductGalleryBeanFactory(final ProductImageBeanFactory productImageBeanFactory) {
        this.productImageBeanFactory = productImageBeanFactory;
    }

    public final ProductGalleryBean create(final ProductProjection product, final ProductVariant variant) {
        final Data data = new Data(product, variant);
        return initializedViewModel(data);
    }

    @Override
    protected ProductGalleryBean getViewModelInstance() {
        return new ProductGalleryBean();
    }

    @Override
    protected final void initialize(final ProductGalleryBean bean, final Data data) {
        fillList(bean, data);
    }

    protected void fillList(final ProductGalleryBean bean, final Data data) {
        bean.setList(data.variant.getImages().stream()
                .map(productImageBeanFactory::create)
                .collect(toList()));
    }

    protected final static class Data extends Base {

        public final ProductProjection product;
        public final ProductVariant variant;

        public Data(final ProductProjection product, final ProductVariant variant) {
            this.product = product;
            this.variant = variant;
        }
    }
}
