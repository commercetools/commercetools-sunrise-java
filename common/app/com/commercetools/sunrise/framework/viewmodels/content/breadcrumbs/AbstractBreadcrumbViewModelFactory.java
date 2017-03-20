package com.commercetools.sunrise.framework.viewmodels.content.breadcrumbs;

import com.commercetools.sunrise.framework.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.mvc.Call;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public abstract class AbstractBreadcrumbViewModelFactory<I> extends SimpleViewModelFactory<BreadcrumbViewModel, I> {

    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    protected AbstractBreadcrumbViewModelFactory(final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
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
    protected BreadcrumbViewModel newViewModelInstance(final I input) {
        return new BreadcrumbViewModel();
    }

    @Override
    protected void initialize(final BreadcrumbViewModel viewModel, final I input) {
        fillLinks(viewModel, input);
    }

    protected abstract void fillLinks(final BreadcrumbViewModel viewModel, final I input);

    protected List<BreadcrumbLinkViewModel> createCategoryTreeLinks(final Category category) {
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

    private BreadcrumbLinkViewModel createCategoryLinkData(final Category category) {
        final BreadcrumbLinkViewModel linkViewModel = new BreadcrumbLinkViewModel();
        linkViewModel.setText(category.getName());
        linkViewModel.setUrl(productReverseRouter
                .productOverviewPageCall(category)
                .map(Call::url)
                .orElse(""));
        return linkViewModel;
    }
}
