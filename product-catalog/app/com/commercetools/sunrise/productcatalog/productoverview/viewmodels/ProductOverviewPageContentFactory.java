package com.commercetools.sunrise.productcatalog.productoverview.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.productcatalog.productoverview.ProductsWithCategory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

public class ProductOverviewPageContentFactory extends PageContentFactory<ProductOverviewPageContent, ProductsWithCategory> {

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
    public final ProductOverviewPageContent create(final ProductsWithCategory data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductOverviewPageContent model, final ProductsWithCategory data) {
        super.initialize(model, data);
        fillProducts(model, data);
        fillFilterProductsUrl(model, data);
        fillBanner(model, data);
        fillBreadcrumb(model, data);
        fillJumbotron(model, data);
        fillSeo(model, data);
    }

    @Override
    protected void fillTitle(final ProductOverviewPageContent model, final ProductsWithCategory productsWithCategory) {
        if (productsWithCategory.getCategory() != null) {
            model.setTitle(productsWithCategory.getCategory().getName().find(singletonList(locale)).orElse(""));
        }
    }

    protected void fillFilterProductsUrl(final ProductOverviewPageContent bean, final ProductsWithCategory productsWithCategory) {
        bean.setFilterProductsUrl(httpRequest.path());
    }

    protected void fillProducts(final ProductOverviewPageContent bean, final ProductsWithCategory productsWithCategory) {
        bean.setProducts(productListBeanFactory.create(productsWithCategory.getProducts().getResults()));
    }

    protected void fillSeo(final ProductOverviewPageContent bean, final ProductsWithCategory productsWithCategory) {
        bean.setSeo(seoBeanFactory.create(productsWithCategory));
    }

    protected void fillBreadcrumb(final ProductOverviewPageContent bean, final ProductsWithCategory productsWithCategory) {
        bean.setBreadcrumb(categoryBreadcrumbBeanFactory.create(productsWithCategory));
    }

    protected void fillJumbotron(final ProductOverviewPageContent bean, final ProductsWithCategory productsWithCategory) {
        bean.setJumbotron(jumbotronBeanFactory.create(productsWithCategory));
    }

    protected void fillBanner(final ProductOverviewPageContent bean, final ProductsWithCategory productsWithCategory) {
        bean.setBanner(bannerBeanFactory.create(productsWithCategory));
    }
}