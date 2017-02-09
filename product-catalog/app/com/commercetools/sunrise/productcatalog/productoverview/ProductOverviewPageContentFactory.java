package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;

import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

public class ProductOverviewPageContentFactory extends PageContentFactory<ProductOverviewPageContent, ProductOverviewControllerData> {

    private final Locale locale;
    private final RequestContext requestContext;
    private final CategoryBreadcrumbBeanFactory categoryBreadcrumbBeanFactory;
    private final ProductListBeanFactory productListBeanFactory;
    private final BannerBeanFactory bannerBeanFactory;
    private final JumbotronBeanFactory jumbotronBeanFactory;
    private final SeoBeanFactory seoBeanFactory;

    @Inject
    public ProductOverviewPageContentFactory(final Locale locale, final RequestContext requestContext,
                                             final CategoryBreadcrumbBeanFactory categoryBreadcrumbBeanFactory,
                                             final ProductListBeanFactory productListBeanFactory, final BannerBeanFactory bannerBeanFactory,
                                             final JumbotronBeanFactory jumbotronBeanFactory, final SeoBeanFactory seoBeanFactory) {
        this.locale = locale;
        this.requestContext = requestContext;
        this.categoryBreadcrumbBeanFactory = categoryBreadcrumbBeanFactory;
        this.productListBeanFactory = productListBeanFactory;
        this.bannerBeanFactory = bannerBeanFactory;
        this.jumbotronBeanFactory = jumbotronBeanFactory;
        this.seoBeanFactory = seoBeanFactory;
    }

    @Override
    protected ProductOverviewPageContent getViewModelInstance() {
        return new ProductOverviewPageContent();
    }


    @Override
    public final ProductOverviewPageContent create(final ProductOverviewControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductOverviewPageContent model, final ProductOverviewControllerData data) {
        super.initialize(model, data);
        fillProducts(model, data);
        fillFilterProductsUrl(model, data);
        fillBanner(model, data);
        fillBreadcrumb(model, data);
        fillJumbotron(model, data);
        fillSeo(model, data);
    }

    @Override
    protected void fillTitle(final ProductOverviewPageContent model, final ProductOverviewControllerData data) {
        if (data.getCategory() != null) {
            model.setTitle(data.getCategory().getName().find(singletonList(locale)).orElse(""));
        }
    }

    protected void fillFilterProductsUrl(final ProductOverviewPageContent bean, final ProductOverviewControllerData data) {
        bean.setFilterProductsUrl(requestContext.getPath());
    }

    protected void fillProducts(final ProductOverviewPageContent bean, final ProductOverviewControllerData data) {
        bean.setProducts(productListBeanFactory.create(data.getProductSearchResult().getResults()));
    }

    protected void fillSeo(final ProductOverviewPageContent bean, final ProductOverviewControllerData data) {
        bean.setSeo(seoBeanFactory.create(data));
    }

    protected void fillBreadcrumb(final ProductOverviewPageContent bean, final ProductOverviewControllerData data) {
        bean.setBreadcrumb(categoryBreadcrumbBeanFactory.create(data));
    }

    protected void fillJumbotron(final ProductOverviewPageContent bean, final ProductOverviewControllerData data) {
        bean.setJumbotron(jumbotronBeanFactory.create(data));
    }

    protected void fillBanner(final ProductOverviewPageContent bean, final ProductOverviewControllerData data) {
        bean.setBanner(bannerBeanFactory.create(data));
    }
}