package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class ProductListBeanFactory extends ViewModelFactory<ProductListBean, Iterable<ProductProjection>> {

    private final ProductThumbnailBeanFactory productThumbnailBeanFactory;

    @Inject
    public ProductListBeanFactory(final ProductThumbnailBeanFactory productThumbnailBeanFactory) {
        this.productThumbnailBeanFactory = productThumbnailBeanFactory;
    }

    @Override
    protected ProductListBean getViewModelInstance() {
        return new ProductListBean();
    }

    @Override
    public final ProductListBean create(final Iterable<ProductProjection> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductListBean model, final Iterable<ProductProjection> data) {
        fillList(model, data);
    }

    protected void fillList(final ProductListBean model, final Iterable<ProductProjection> products) {
        final List<ProductThumbnailBean> list = new ArrayList<>();
        products.forEach(product -> list.add(productThumbnailBeanFactory.create(createProductWithVariant(product))));
        model.setList(list);
    }

    private ProductWithVariant createProductWithVariant(final ProductProjection product) {
        final ProductVariant selectedVariant = product.findFirstMatchingVariant()
                .orElseGet(product::getMasterVariant);
        return new ProductWithVariant(product, selectedVariant);
    }
}
