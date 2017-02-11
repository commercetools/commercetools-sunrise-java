package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductSimpleReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import java.util.*;

import static java.util.stream.Collectors.toList;

public abstract class AbstractBreadcrumbBeanFactory<D> extends ViewModelFactory<BreadcrumbBean, D> {

    private final Locale locale;
    private final CategoryTree categoryTree;
    private final ProductSimpleReverseRouter productReverseRouter;

    protected AbstractBreadcrumbBeanFactory(final Locale locale, final CategoryTree categoryTree, final ProductSimpleReverseRouter productReverseRouter) {
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    protected BreadcrumbBean getViewModelInstance() {
        return new BreadcrumbBean();
    }

    @Override
    protected void initialize(final BreadcrumbBean model, final D data) {
        fillLinks(model, data);
    }

    protected abstract void fillLinks(final BreadcrumbBean bean, final D data);

    protected List<BreadcrumbLinkBean> createCategoryTreeLinks(final Category category) {
        return getCategoryWithAncestors(category).stream()
                    .map(this::createCategoryLinkData)
                    .collect(toList());
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

    protected List<BreadcrumbLinkBean> createProductLinks(final ProductWithVariant productWithVariant) {
        final List<BreadcrumbLinkBean> categoryTreeLinks = createCategoryTreeLinks(productWithVariant);
        final List<BreadcrumbLinkBean> result = new ArrayList<>(1 + categoryTreeLinks.size());
        result.addAll(categoryTreeLinks);
        result.add(createProductLink(productWithVariant));
        return result;
    }

    private List<BreadcrumbLinkBean> createCategoryTreeLinks(final ProductWithVariant productWithVariant) {
        return productWithVariant.getProduct().getCategories().stream()
                .findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
    }

    private BreadcrumbLinkBean createCategoryLinkData(final Category category) {
        final BreadcrumbLinkBean linkBean = new BreadcrumbLinkBean();
        linkBean.setText(category.getName());
        linkBean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(locale, category));
        return linkBean;
    }

    private BreadcrumbLinkBean createProductLink(final ProductWithVariant productWithVariant) {
        final BreadcrumbLinkBean linkBean = new BreadcrumbLinkBean();
        linkBean.setText(productWithVariant.getProduct().getName());
        linkBean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, productWithVariant.getProduct(), productWithVariant.getVariant()));
        return linkBean;
    }
}
