package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.categories.CategoryBean;
import com.commercetools.sunrise.common.models.categories.CategoryBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class PageNavMenuFactory extends ViewModelFactory<PageNavMenu, Void> {

    private final CategoryTree categoryTree;
    private final CategoryBeanFactory categoryBeanFactory;

    @Inject
    public PageNavMenuFactory(final CategoryTree categoryTree, final CategoryBeanFactory categoryBeanFactory) {
        this.categoryTree = categoryTree;
        this.categoryBeanFactory = categoryBeanFactory;
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

    protected void fillCategories(final PageNavMenu bean) {
        final List<CategoryBean> categories = new LinkedList<>();
        categoryTree.getRoots().forEach(root -> categories.add(categoryBeanFactory.create(root)));
        bean.setCategories(categories);
    }
}
