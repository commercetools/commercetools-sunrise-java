package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.google.inject.Inject;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class BreadcrumbBeanFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private ProductReverseRouter productReverseRouter;

    public BreadcrumbBean create(final ProductProjection product, final ProductVariant variant) {
        final BreadcrumbBean bean = new BreadcrumbBean();
        initialize(bean, product, variant);
        return bean;
    }

    public BreadcrumbBean create(final Category category) {
        final BreadcrumbBean bean = new BreadcrumbBean();
        initialize(bean, category);
        return bean;
    }

    protected final void initialize(final BreadcrumbBean bean, final ProductProjection product, final ProductVariant variant) {
        fillLinks(bean, createLinkBeanList(product, variant));
    }

    protected final void initialize(final BreadcrumbBean bean, final Category category) {
        fillLinks(bean, createCategoryTreeLinks(category));
    }

    protected void fillLinks(final BreadcrumbBean breadcrumbBean, final List<LinkBean> linkBeans) {
        breadcrumbBean.setLinks(linkBeans);
    }

    protected List<LinkBean> createCategoryLinkBeanList(final ProductProjection product) {
        return product.getCategories().stream()
                .findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
    }

    protected List<LinkBean> createLinkBeanList(final ProductProjection product, final ProductVariant variant) {
        final List<LinkBean> linkBeans = createCategoryLinkBeanList(product);
        final LinkBean productLinkData = createProductLinkData(product, variant);
        final List<LinkBean> result = new ArrayList<>(1 + linkBeans.size());
        result.addAll(linkBeans);
        result.add(productLinkData);
        return result;
    }

    protected List<LinkBean> createCategoryTreeLinks(final Category category) {
        return getCategoryWithAncestors(category).stream()
                .map(this::createCategoryLinkData)
                .collect(toList());
    }

    protected LinkBean createCategoryLinkData(final Category category) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(category.getName().find(userContext.locales()).orElse(""));
        linkBean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(userContext.locale(), category));
        return linkBean;
    }

    protected LinkBean createProductLinkData(final ProductProjection product, final ProductVariant variant) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(product.getName().find(userContext.locales()).orElse(""));
        linkBean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), product, variant));
        return linkBean;
    }

    protected List<Category> getCategoryWithAncestors(final Category category) {
        final List<Category> ancestors = category.getAncestors().stream()
                .map(ref -> categoryTree.findById(ref.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        ancestors.add(category);
        return ancestors;
    }
}
