package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import java.util.*;

import static java.util.stream.Collectors.toList;

public abstract class AbstractBreadcrumbBeanFactory<D> extends ViewModelFactory<BreadcrumbBean, D> {

    private final Locale locale;
    private final CategoryTree categoryTree;
    private final LocalizedStringResolver localizedStringResolver;
    private final ProductReverseRouter productReverseRouter;

    public AbstractBreadcrumbBeanFactory(final Locale locale, final CategoryTree categoryTree,
                                         final LocalizedStringResolver localizedStringResolver, final ProductReverseRouter productReverseRouter) {
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.localizedStringResolver = localizedStringResolver;
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

    protected List<LinkBean> createCategoryTreeLinks(final Category category) {
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

    protected List<LinkBean> createProductLinks(final ProductWithVariant productWithVariant) {
        final List<LinkBean> categoryTreeLinks = createCategoryTreeLinks(productWithVariant);
        final List<LinkBean> result = new ArrayList<>(1 + categoryTreeLinks.size());
        result.addAll(categoryTreeLinks);
        result.add(createProductLink(productWithVariant));
        return result;
    }

    private List<LinkBean> createCategoryTreeLinks(final ProductWithVariant productWithVariant) {
        return productWithVariant.getProduct().getCategories().stream()
                .findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
    }

    private LinkBean createCategoryLinkData(final Category category) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(localizedStringResolver.getOrEmpty(category.getName()));
        linkBean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(locale, category));
        return linkBean;
    }

    private LinkBean createProductLink(final ProductWithVariant productWithVariant) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(localizedStringResolver.getOrEmpty(productWithVariant.getProduct().getName()));
        linkBean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, productWithVariant.getProduct(), productWithVariant.getVariant()));
        return linkBean;
    }
}
