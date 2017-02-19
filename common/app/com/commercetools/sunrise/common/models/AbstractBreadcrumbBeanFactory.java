package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.framework.reverserouters.productcatalog.ProductReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public abstract class AbstractBreadcrumbBeanFactory<D> extends ViewModelFactory<BreadcrumbBean, D> {

    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    protected AbstractBreadcrumbBeanFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    protected final CategoryTree getCategoryTree() {
        return categoryTree;
    }

    protected final ProductReverseRouter getProductReverseRouter() {
        return productReverseRouter;
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

    private BreadcrumbLinkBean createCategoryLinkData(final Category category) {
        final BreadcrumbLinkBean linkBean = new BreadcrumbLinkBean();
        linkBean.setText(category.getName());
        linkBean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(category));
        return linkBean;
    }
}
