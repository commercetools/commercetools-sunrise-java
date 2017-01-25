package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.productcatalog.common.AbstractBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class CategoryBreadcrumbBeanFactory extends AbstractBreadcrumbBeanFactory<ProductOverviewPageData> {

    @Inject
    public CategoryBreadcrumbBeanFactory(final Locale locale, final CategoryTree categoryTree,
                                         final LocalizedStringResolver localizedStringResolver, final ProductReverseRouter productReverseRouter) {
        super(locale, categoryTree, localizedStringResolver, productReverseRouter);
    }

    @Override
    public final BreadcrumbBean create(final ProductOverviewPageData data) {
        return super.create(data);
    }

    @Override
    protected BreadcrumbBean getViewModelInstance() {
        return new BreadcrumbBean();
    }

    @Override
    protected final void initialize(final BreadcrumbBean model, final ProductOverviewPageData data) {
        super.initialize(model, data);
    }

    @Override
    protected void fillLinks(final BreadcrumbBean bean, final ProductOverviewPageData data) {
        bean.setLinks(createCategoryTreeLinks(data.category));
    }
}
