package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.models.categories.CategoryViewModel;
import com.commercetools.sunrise.common.models.categories.CategoryViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class PageNavMenuFactory extends ViewModelFactory<PageNavMenu, Void> {

    private final CategoryTree categoryTree;
    private final CategoryViewModelFactory categoryViewModelFactory;

    @Inject
    public PageNavMenuFactory(final CategoryTree categoryTree, final CategoryViewModelFactory categoryViewModelFactory) {
        this.categoryTree = categoryTree;
        this.categoryViewModelFactory = categoryViewModelFactory;
    }

    @Override
    protected PageNavMenu getViewModelInstance(final Void input) {
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
