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
    private final CategoryBreadcrumbViewModelFactory categoryBreadcrumbViewModelFactory;
    private final ProductListViewModelFactory productListViewModelFactory;
    private final BannerViewModelFactory bannerViewModelFactory;
    private final JumbotronViewModelFactory jumbotronViewModelFactory;
    private final SeoViewModelFactory seoViewModelFactory;

    @Inject
    public ProductOverviewPageContentFactory(final Locale locale, final Http.Request httpRequest,
                                             final CategoryBreadcrumbViewModelFactory categoryBreadcrumbViewModelFactory,
                                             final ProductListViewModelFactory productListViewModelFactory, final BannerViewModelFactory bannerViewModelFactory,
                                             final JumbotronViewModelFactory jumbotronViewModelFactory, final SeoViewModelFactory seoViewModelFactory) {
        this.locale = locale;
        this.httpRequest = httpRequest;
        this.categoryBreadcrumbViewModelFactory = categoryBreadcrumbViewModelFactory;
        this.productListViewModelFactory = productListViewModelFactory;
        this.bannerViewModelFactory = bannerViewModelFactory;
        this.jumbotronViewModelFactory = jumbotronViewModelFactory;
        this.seoViewModelFactory = seoViewModelFactory;
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

    protected void fillFilterProductsUrl(final ProductOverviewPageContent model, final ProductsWithCategory productsWithCategory) {
        model.setFilterProductsUrl(httpRequest.path());
    }

    protected void fillProducts(final ProductOverviewPageContent model, final ProductsWithCategory productsWithCategory) {
        model.setProducts(productListViewModelFactory.create(productsWithCategory.getProducts().getResults()));
    }

    protected void fillSeo(final ProductOverviewPageContent model, final ProductsWithCategory productsWithCategory) {
        model.setSeo(seoViewModelFactory.create(productsWithCategory));
    }

    protected void fillBreadcrumb(final ProductOverviewPageContent model, final ProductsWithCategory productsWithCategory) {
        model.setBreadcrumb(categoryBreadcrumbViewModelFactory.create(productsWithCategory));
    }

    protected void fillJumbotron(final ProductOverviewPageContent model, final ProductsWithCategory productsWithCategory) {
        model.setJumbotron(jumbotronViewModelFactory.create(productsWithCategory));
    }

    protected void fillBanner(final ProductOverviewPageContent model, final ProductsWithCategory productsWithCategory) {
        model.setBanner(bannerViewModelFactory.create(productsWithCategory));
    }
}