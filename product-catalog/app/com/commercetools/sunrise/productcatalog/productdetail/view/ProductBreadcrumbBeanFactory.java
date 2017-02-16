package com.commercetools.sunrise.productcatalog.productdetail.view;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.common.AbstractBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class ProductBreadcrumbBeanFactory extends AbstractBreadcrumbBeanFactory<ProductWithVariant> {

    @Inject
    public ProductBreadcrumbBeanFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        super(categoryTree, productReverseRouter);
    }

    @Override
    public final BreadcrumbBean create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected BreadcrumbBean getViewModelInstance() {
        return new BreadcrumbBean();
    }

    @Override
    protected final void initialize(final BreadcrumbBean model, final ProductWithVariant productWithVariant) {
        super.initialize(model, productWithVariant);
    }

    @Override
    protected void fillLinks(final BreadcrumbBean bean, final ProductWithVariant productWithVariant) {
        bean.setLinks(createProductLinks(productWithVariant));
    }
}
