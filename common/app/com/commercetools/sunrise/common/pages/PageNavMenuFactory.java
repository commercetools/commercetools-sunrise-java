package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.CategoryBean;
import com.commercetools.sunrise.common.models.CategoryBeanFactory;
import com.commercetools.sunrise.common.models.CommonViewModelFactory;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class PageNavMenuFactory extends CommonViewModelFactory<PageNavMenu> {

    private final CategoryTree categoryTree;
    private final CategoryBeanFactory categoryBeanFactory;

    @Inject
    public PageNavMenuFactory(final CategoryTree categoryTree, final CategoryBeanFactory categoryBeanFactory) {
        this.categoryTree = categoryTree;
        this.categoryBeanFactory = categoryBeanFactory;
    }

    public final PageNavMenu create() {
        return initializedViewModel();
    }

    @Override
    protected PageNavMenu getViewModelInstance() {
        return new PageNavMenu();
    }

    @Override
    protected final void initialize(final PageNavMenu bean) {
        fillCategories(bean);
    }

    protected void fillCategories(final PageNavMenu bean) {
        final List<CategoryBean> categories = new LinkedList<>();
        categoryTree.getRoots().forEach(root -> categories.add(categoryBeanFactory.create(root)));
        bean.setCategories(categories);
    }
}
