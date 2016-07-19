package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.InfoData;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.ProductListBeanFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

public class ProductOverviewPageContentFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private RequestContext requestContext;
    @Inject
    private BreadcrumbBeanFactory breadcrumbBeanFactory;
    @Inject
    private ProductListBeanFactory productListBeanFactory;

    public ProductOverviewPageContent create(@Nullable final Category category, final PagedSearchResult<ProductProjection> searchResult) {
        final ProductOverviewPageContent bean = new ProductOverviewPageContent();
        initialize(bean, category, searchResult);
        return bean;
    }

    protected final void initialize(final ProductOverviewPageContent bean, final @Nullable Category category, final PagedSearchResult<ProductProjection> searchResult) {
        fillProducts(bean, searchResult);
        fillFilterProductsUrl(bean);
        if (category != null) {
            fillCategoryInfo(bean, category);
        }
    }

    protected void fillFilterProductsUrl(final ProductOverviewPageContent bean) {
        bean.setFilterProductsUrl(requestContext.getPath());
    }

    protected void fillProducts(final ProductOverviewPageContent bean, final PagedSearchResult<ProductProjection> searchResult) {
        bean.setProducts(productListBeanFactory.create(searchResult.getResults()));
    }

    protected void fillCategoryInfo(final ProductOverviewPageContent bean, final Category category) {
        bean.setBreadcrumb(breadcrumbBeanFactory.create(category));
        bean.setTitle(category.getName().find(userContext.locales()).orElse(""));
        bean.setJumbotron(createJumbotron(category));
        bean.setBanner(createBanner(category));
        bean.setSeo(createSeo(category));
    }

    protected BannerBean createBanner(final Category category) {
        final BannerBean bean = new BannerBean();
        bean.setTitle(category.getName().find(userContext.locales()).orElse(""));
        Optional.ofNullable(category.getDescription())
                .ifPresent(description -> bean.setDescription(description.find(userContext.locales()).orElse("")));
        bean.setImageMobile("/assets/img/banner_mobile-0a9241da249091a023ecfadde951a53b.jpg"); // TODO obtain from category?
        bean.setImageDesktop("/assets/img/banner_desktop-9ffd148c48068ce2666d6533b4a87d11.jpg"); // TODO obtain from category?
        return bean;
    }

    protected JumbotronBean createJumbotron(final Category category) {
        final JumbotronBean bean = new JumbotronBean();
        bean.setTitle(category.getName().find(userContext.locales()).orElse(""));
        Optional.ofNullable(category.getParent())
                .ifPresent(parentRef -> categoryTree.findById(parentRef.getId())
                        .ifPresent(parent -> bean.setSubtitle(parent.getName().find(userContext.locales()).orElse(""))));
        Optional.ofNullable(category.getDescription())
                .ifPresent(description -> bean.setDescription(description.find(userContext.locales()).orElse("")));
        return bean;
    }

    protected InfoData createSeo(final Category category) {
        final InfoData bean = new InfoData();
        Optional.ofNullable(category.getMetaTitle())
                .ifPresent(title -> bean.setTitle(title.find(userContext.locales()).orElse("")));
        Optional.ofNullable(category.getMetaDescription())
                .ifPresent(description -> bean.setDescription(description.find(userContext.locales()).orElse("")));
        return bean;
    }
}
