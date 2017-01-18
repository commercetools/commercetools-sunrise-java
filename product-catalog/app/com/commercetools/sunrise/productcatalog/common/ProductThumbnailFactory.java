package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
public class ProductThumbnailFactory extends ViewModelFactory {

    private final CategoryTree categoryTreeInNew;
    private final ProductBeanFactory productBeanFactory;

    @Inject
    public ProductThumbnailFactory(@Named("new") final CategoryTree categoryTreeInNew, final ProductBeanFactory productBeanFactory) {
        this.categoryTreeInNew = categoryTreeInNew;
        this.productBeanFactory = productBeanFactory;
    }

    public ProductThumbnailBean create(final ProductProjection product, final ProductVariant variant) {
        final ProductThumbnailBean bean = new ProductThumbnailBean();
        initialize(bean, product, variant);
        return bean;
    }

    protected final void initialize(final ProductThumbnailBean bean, final ProductProjection product, final ProductVariant variant) {
        fillProduct(bean, product, variant);
        fillNew(bean, product, variant);
        fillSale(bean, product, variant);
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
}
