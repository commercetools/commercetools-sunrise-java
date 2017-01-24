package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import javax.inject.Named;

import static com.commercetools.sunrise.common.utils.ProductPriceUtils.hasDiscount;

@RequestScoped
public class ProductThumbnailFactory extends ViewModelFactory<ProductThumbnailBean, ProductThumbnailFactory.Data> {

    private final CategoryTree categoryTreeInNew;
    private final ProductBeanFactory productBeanFactory;

    @Inject
    public ProductThumbnailFactory(@Named("new") final CategoryTree categoryTreeInNew, final ProductBeanFactory productBeanFactory) {
        this.categoryTreeInNew = categoryTreeInNew;
        this.productBeanFactory = productBeanFactory;
    }

    public final ProductThumbnailBean create(final ProductProjection product, final ProductVariant variant) {
        final Data data = new Data(product, variant);
        return initializedViewModel(data);
    }

    @Override
    protected ProductThumbnailBean getViewModelInstance() {
        return new ProductThumbnailBean();
    }

    @Override
    protected final void initialize(final ProductThumbnailBean bean, final Data data) {
        fillProduct(bean, data);
        fillNew(bean, data);
        fillSale(bean, data);
    }

    protected void fillProduct(final ProductThumbnailBean bean, final Data data) {
        bean.setProduct(productBeanFactory.create(data.product, data.variant));
    }

    protected void fillNew(final ProductThumbnailBean bean, final Data data) {
        bean.setNew(data.product.getCategories().stream()
                .anyMatch(category -> categoryTreeInNew.findById(category.getId()).isPresent()));
    }

    protected void fillSale(final ProductThumbnailBean bean, final Data data) {
        bean.setSale(hasDiscount(data.variant));
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
