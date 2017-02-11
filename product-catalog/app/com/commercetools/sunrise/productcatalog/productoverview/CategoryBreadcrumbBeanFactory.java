package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductSimpleReverseRouter;
import com.commercetools.sunrise.productcatalog.common.AbstractBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class CategoryBreadcrumbBeanFactory extends AbstractBreadcrumbBeanFactory<ProductOverviewControllerData> {

    @Inject
    public CategoryBreadcrumbBeanFactory(final Locale locale, final CategoryTree categoryTree, final ProductSimpleReverseRouter productReverseRouter) {
        super(locale, categoryTree, productReverseRouter);
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
