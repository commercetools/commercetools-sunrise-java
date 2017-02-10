package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

public class ProductOverviewPageContentFactory extends PageContentFactory<ProductOverviewPageContent, ProductOverviewControllerData> {

    private final Locale locale;
    private final Http.Request httpRequest;
    private final CategoryBreadcrumbBeanFactory categoryBreadcrumbBeanFactory;
    private final ProductListBeanFactory productListBeanFactory;
    private final BannerBeanFactory bannerBeanFactory;
    private final JumbotronBeanFactory jumbotronBeanFactory;
    private final SeoBeanFactory seoBeanFactory;

    @Inject
    public ProductOverviewPageContentFactory(final Locale locale, final Http.Request httpRequest,
                                             final CategoryBreadcrumbBeanFactory categoryBreadcrumbBeanFactory,
                                             final ProductListBeanFactory productListBeanFactory, final BannerBeanFactory bannerBeanFactory,
                                             final JumbotronBeanFactory jumbotronBeanFactory, final SeoBeanFactory seoBeanFactory) {
        this.locale = locale;
        this.httpRequest = httpRequest;
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
        bean.setFilterProductsUrl(httpRequest.path());
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