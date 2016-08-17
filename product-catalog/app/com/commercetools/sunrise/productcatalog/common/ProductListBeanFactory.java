package com.commercetools.sunrise.productcatalog.common;

import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class ProductListBeanFactory extends Base {

    @Inject
    @Named("new")
    private CategoryTree categoryTreeInNew;
    @Inject
    private ProductBeanFactory productBeanFactory;

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
                .map(product -> createThumbnail(product, getSelectedVariant(product)))
                .collect(toList()));
    }

    protected ProductThumbnailBean createThumbnail(final ProductProjection product, final ProductVariant variant) {
        final ProductThumbnailBean bean = new ProductThumbnailBean();
        fillProduct(bean, product, variant);
        fillNew(bean, product, variant);
        fillSale(bean, product, variant);
        return bean;
    }

    protected void fillProduct(final ProductThumbnailBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setProduct(productBeanFactory.create(product, variant));
    }

    protected void fillNew(final ProductThumbnailBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setNew(product.getCategories().stream()
                .anyMatch(category -> categoryTreeInNew.findById(category.getId()).isPresent()));
    }

    protected void fillSale(final ProductThumbnailBean bean, final ProductProjection product, final ProductVariant variant) {
        final boolean isSale = bean.getProduct() != null && bean.getProduct().getVariant() != null
                && bean.getProduct().getVariant().getPriceOld() != null;
        bean.setSale(isSale);
    }

    protected ProductVariant getSelectedVariant(final ProductProjection product) {
        return product.findFirstMatchingVariant()
                .orElseGet(product::getMasterVariant);
    }
}
