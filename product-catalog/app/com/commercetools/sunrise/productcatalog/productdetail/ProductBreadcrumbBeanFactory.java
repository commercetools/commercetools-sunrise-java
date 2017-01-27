package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.productcatalog.common.AbstractBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class ProductBreadcrumbBeanFactory extends AbstractBreadcrumbBeanFactory<ProductDetailControllerData> {

    @Inject
    public ProductBreadcrumbBeanFactory(final Locale locale, final CategoryTree categoryTree,
                                        final LocalizedStringResolver localizedStringResolver, final ProductReverseRouter productReverseRouter) {
        super(locale, categoryTree, localizedStringResolver, productReverseRouter);
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
        bean.setLinks(createProductLinks(data.productWithVariant));
    }
}
