package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.models.TitleDescriptionBean;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.productcatalog.common.CategoryBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class ProductOverviewPageContentFactory extends PageContentFactory {

    private final LocalizedStringResolver localizedStringResolver;
    private final CategoryTree categoryTree;
    private final RequestContext requestContext;
    private final CategoryBreadcrumbBeanFactory breadcrumbBeanFactory;
    private final ProductListBeanFactory productListBeanFactory;

    @Inject
    public ProductOverviewPageContentFactory(final LocalizedStringResolver localizedStringResolver, final CategoryTree categoryTree,
                                             final RequestContext requestContext, final CategoryBreadcrumbBeanFactory breadcrumbBeanFactory,
                                             final ProductListBeanFactory productListBeanFactory) {
        this.localizedStringResolver = localizedStringResolver;
        this.categoryTree = categoryTree;
        this.requestContext = requestContext;
        this.breadcrumbBeanFactory = breadcrumbBeanFactory;
        this.productListBeanFactory = productListBeanFactory;
    }

    public ProductOverviewPageContent create(final PagedSearchResult<ProductProjection> searchResult, @Nullable final Category category) {
        final ProductOverviewPageContent bean = new ProductOverviewPageContent();
        initialize(bean, searchResult, category);
        return bean;
    }

    protected final void initialize(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, @Nullable final Category category) {
        fillProducts(bean, searchResult, category);
        fillFilterProductsUrl(bean, searchResult, category);
        if (category != null) {
            fillBanner(bean, searchResult, category);
            fillBreadcrumb(bean, searchResult, category);
            fillTitle(bean, searchResult, category);
            fillJumbotron(bean, searchResult, category);
            fillSeo(bean, searchResult, category);
        }
    }

    protected void fillFilterProductsUrl(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, @Nullable final Category category) {
        bean.setFilterProductsUrl(requestContext.getPath());
    }

    protected void fillProducts(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, @Nullable final Category category) {
        bean.setProducts(productListBeanFactory.create(searchResult.getResults()));
    }

    protected void fillSeo(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, final Category category) {
        bean.setSeo(createSeo(category));
    }

    protected void fillBreadcrumb(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, final Category category) {
        bean.setBreadcrumb(breadcrumbBeanFactory.create(category));
    }

    protected void fillTitle(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, final Category category) {
        bean.setTitle(localizedStringResolver.getOrEmpty(category.getName()));
    }

    protected void fillJumbotron(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, final Category category) {
        bean.setJumbotron(createJumbotron(category));
    }

    protected void fillBanner(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult, final Category category) {
        bean.setBanner(createBanner(category));
    }

    private BannerBean createBanner(final Category category) {
        final BannerBean bean = new BannerBean();
        bean.setTitle(localizedStringResolver.getOrEmpty(category.getName()));
        Optional.ofNullable(category.getDescription())
                .ifPresent(description -> bean.setDescription(localizedStringResolver.getOrEmpty(description)));
        return bean;
    }

    private JumbotronBean createJumbotron(final Category category) {
        final JumbotronBean bean = new JumbotronBean();
        bean.setTitle(localizedStringResolver.getOrEmpty(category.getName()));
        Optional.ofNullable(category.getParent())
                .ifPresent(parentRef -> categoryTree.findById(parentRef.getId())
                        .ifPresent(parent -> bean.setSubtitle(localizedStringResolver.getOrEmpty(parent.getName()))));
        Optional.ofNullable(category.getDescription())
                .ifPresent(description -> bean.setDescription(localizedStringResolver.getOrEmpty(description)));
        return bean;
    }

    private TitleDescriptionBean createSeo(final Category category) {
        final TitleDescriptionBean bean = new TitleDescriptionBean();
        Optional.ofNullable(category.getMetaTitle())
                .ifPresent(title -> bean.setTitle(localizedStringResolver.getOrEmpty(title)));
        Optional.ofNullable(category.getMetaDescription())
                .ifPresent(description -> bean.setDescription(localizedStringResolver.getOrEmpty(description)));
        return bean;
    }
}
