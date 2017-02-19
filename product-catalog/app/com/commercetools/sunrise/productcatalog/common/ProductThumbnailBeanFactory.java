package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import javax.inject.Named;

import static com.commercetools.sunrise.common.utils.ProductPriceUtils.hasDiscount;

@RequestScoped
public class ProductThumbnailBeanFactory extends ViewModelFactory<ProductThumbnailBean, ProductWithVariant> {

    private final CategoryTree categoryTreeInNew;
    private final ProductBeanFactory productBeanFactory;

    @Inject
    public ProductThumbnailBeanFactory(@Named("new") final CategoryTree categoryTreeInNew, final ProductBeanFactory productBeanFactory) {
        this.categoryTreeInNew = categoryTreeInNew;
        this.productBeanFactory = productBeanFactory;
    }

    @Override
    protected ProductThumbnailBean getViewModelInstance() {
        return new ProductThumbnailBean();
    }

    @Override
    public final ProductThumbnailBean create(final ProductWithVariant data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductThumbnailBean model, final ProductWithVariant data) {
        fillProduct(model, data);
        fillNew(model, data);
        fillSale(model, data);
    }

    protected void fillProduct(final ProductThumbnailBean model, final ProductWithVariant productWithVariant) {
        model.setProduct(productBeanFactory.create(productWithVariant));
    }

    protected void fillNew(final ProductThumbnailBean model, final ProductWithVariant productWithVariant) {
        model.setNew(productWithVariant.getProduct().getCategories().stream()
                .anyMatch(category -> categoryTreeInNew.findById(category.getId()).isPresent()));
    }

    protected void fillSale(final ProductThumbnailBean model, final ProductWithVariant productWithVariant) {
        model.setSale(hasDiscount(productWithVariant.getVariant()));
    }
}
