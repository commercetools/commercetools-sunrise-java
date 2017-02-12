package com.commercetools.sunrise.productcatalog.productoverview.view;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.productcatalog.common.AbstractBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewControllerData;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;

@RequestScoped
public class CategoryBreadcrumbBeanFactory extends AbstractBreadcrumbBeanFactory<ProductOverviewControllerData> {

    @Inject
    public CategoryBreadcrumbBeanFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        super(categoryTree, productReverseRouter);
    }

    @Override
    public final BreadcrumbBean create(final ProductOverviewControllerData data) {
        return super.create(data);
    }

    @Override
    protected BreadcrumbBean getViewModelInstance() {
        return new BreadcrumbBean();
    }

    @Override
    protected final void initialize(final BreadcrumbBean model, final ProductOverviewControllerData data) {
        super.initialize(model, data);
    }

    @Override
    protected void fillLinks(final BreadcrumbBean bean, final ProductOverviewControllerData data) {
        if (data.getCategory() != null) {
            bean.setLinks(createCategoryTreeLinks(data.getCategory()));
        }
    }
}
