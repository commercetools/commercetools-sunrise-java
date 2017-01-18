package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductListBeanFactory extends ViewModelFactory {

    private final ProductThumbnailFactory productThumbnailFactory;

    @Inject
    public ProductListBeanFactory(final ProductThumbnailFactory productThumbnailFactory) {
        this.productThumbnailFactory = productThumbnailFactory;
    }

    public ProductListBean create(final Iterable<ProductProjection> productList) {
        final ProductListBean bean = new ProductListBean();
        initialize(bean, productList);
        return bean;
    }

    protected final void initialize(final ProductListBean bean, final Iterable<ProductProjection> productList) {
        fillList(bean, productList);
    }

    protected void fillList(final ProductListBean bean, final Iterable<ProductProjection> productList) {
        bean.setList(StreamSupport.stream(productList.spliterator(), false)
                .map(product -> productThumbnailFactory.create(product, getSelectedVariant(product)))
                .collect(toList()));
    }

    private ProductVariant getSelectedVariant(final ProductProjection product) {
        return product.findFirstMatchingVariant()
                .orElseGet(product::getMasterVariant);
    }
}
