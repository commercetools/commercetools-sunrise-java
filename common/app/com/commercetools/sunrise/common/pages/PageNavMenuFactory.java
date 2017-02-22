package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.categories.CategoryViewModel;
import com.commercetools.sunrise.common.models.categories.CategoryViewModelFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
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
    protected PageNavMenu getViewModelInstance() {
        return new PageNavMenu();
    }

    public final PageNavMenu create(final Void data) {
        return initializedViewModel(data);
    }

    @Override
    protected final void initialize(final PageNavMenu model, final Void data) {
        fillCategories(model);
    }

    protected void fillCategories(final PageNavMenu model) {
        final List<CategoryViewModel> categories = new LinkedList<>();
        categoryTree.getRoots().forEach(root -> categories.add(categoryViewModelFactory.create(root)));
        model.setCategories(categories);
    }
}
