package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.common.AbstractBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class ProductBreadcrumbBeanFactory extends AbstractBreadcrumbBeanFactory<ProductDetailControllerData> {

    @Inject
    public ProductBreadcrumbBeanFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        super(categoryTree, productReverseRouter);
    }

    @Override
    public final BreadcrumbBean create(final ProductDetailControllerData data) {
        return super.create(data);
    }

    @Override
    protected BreadcrumbBean getViewModelInstance() {
        return new BreadcrumbBean();
    }

    @Override
    protected final void initialize(final BreadcrumbBean model, final ProductDetailControllerData data) {
        super.initialize(model, data);
    }

    @Override
    protected void fillLinks(final BreadcrumbBean bean, final ProductDetailControllerData data) {
        bean.setLinks(createProductLinks(data.getProductWithVariant()));
    }
}
