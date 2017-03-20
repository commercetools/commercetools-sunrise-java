package com.commercetools.sunrise.framework.viewmodels.header;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.content.categories.CategoryViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.categories.CategoryViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class PageNavMenuFactory extends SimpleViewModelFactory<PageNavMenu, Void> {

    private final CategoryTree categoryTree;
    private final CategoryViewModelFactory categoryViewModelFactory;

    @Inject
    public PageNavMenuFactory(final CategoryTree categoryTree, final CategoryViewModelFactory categoryViewModelFactory) {
        this.categoryTree = categoryTree;
        this.categoryViewModelFactory = categoryViewModelFactory;
    }

    protected final CategoryTree getCategoryTree() {
        return categoryTree;
    }

    protected final CategoryViewModelFactory getCategoryViewModelFactory() {
        return categoryViewModelFactory;
    }

    @Override
    protected PageNavMenu newViewModelInstance(final Void input) {
        return new PageNavMenu();
    }

    @Override
    public final PageNavMenu create(final Void input) {
        return super.create(input);
    }

    public final PageNavMenu create() {
        return super.create(null);
    }

    @Override
    protected final void initialize(final PageNavMenu viewModel, final Void input) {
        fillCategories(viewModel);
    }

    protected void fillCategories(final PageNavMenu viewModel) {
        final List<CategoryViewModel> categories = new LinkedList<>();
        categoryTree.getRoots().forEach(root -> categories.add(categoryViewModelFactory.create(root)));
        viewModel.setCategories(categories);
    }
}
