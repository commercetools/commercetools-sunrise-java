package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class BreadcrumbBeanFactory extends ViewModelFactory {

    private final Locale locale;
    private final LocalizedStringResolver localizedStringResolver;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public BreadcrumbBeanFactory(final Locale locale, final LocalizedStringResolver localizedStringResolver,
                                 final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        this.locale = locale;
        this.localizedStringResolver = localizedStringResolver;
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

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
        fillLinks(bean, product, variant);
    }

    protected final void initialize(final BreadcrumbBean bean, final Category category) {
        fillLinks(bean, category);
    }

    protected void fillLinks(final BreadcrumbBean breadcrumbBean, final ProductProjection product, final ProductVariant variant) {
        breadcrumbBean.setLinks(createLinkBeanList(product, variant));
    }

    protected void fillLinks(final BreadcrumbBean breadcrumbBean, final Category category) {
        breadcrumbBean.setLinks(createCategoryTreeLinks(category));
    }

    private List<LinkBean> createLinkBeanList(final ProductProjection product, final ProductVariant variant) {
        final List<LinkBean> linkBeans = createCategoryLinkBeanList(product);
        final LinkBean productLinkData = createProductLinkData(product, variant);
        final List<LinkBean> result = new ArrayList<>(1 + linkBeans.size());
        result.addAll(linkBeans);
        result.add(productLinkData);
        return result;
    }

    private List<LinkBean> createCategoryLinkBeanList(final ProductProjection product) {
        return product.getCategories().stream()
                .findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
    }

    private List<LinkBean> createCategoryTreeLinks(final Category category) {
        return getCategoryWithAncestors(category).stream()
                .map(this::createCategoryLinkData)
                .collect(toList());
    }

    private LinkBean createCategoryLinkData(final Category category) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(localizedStringResolver.getOrEmpty(category.getName()));
        linkBean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(locale, category));
        return linkBean;
    }

    private LinkBean createProductLinkData(final ProductProjection product, final ProductVariant variant) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(localizedStringResolver.getOrEmpty(product.getName()));
        linkBean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, product, variant));
        return linkBean;
    }

    private List<Category> getCategoryWithAncestors(final Category category) {
        final List<Category> ancestors = category.getAncestors().stream()
                .map(ref -> categoryTree.findById(ref.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        ancestors.add(category);
        return ancestors;
    }
}
