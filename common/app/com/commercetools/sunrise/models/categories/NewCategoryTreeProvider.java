package com.commercetools.sunrise.models.categories;

import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import static java.util.Collections.emptyList;

@Singleton
public final class NewCategoryTreeProvider implements Provider<CategoryTree> {

    private final CategorySettings categorySettings;
    private final Provider<CategoryTree> categoryTreeProvider;

    @Inject
    NewCategoryTreeProvider(final CategorySettings categorySettings, final Provider<CategoryTree> categoryTreeProvider) {
        this.categorySettings = categorySettings;
        this.categoryTreeProvider = categoryTreeProvider;
    }

    @Override
    public CategoryTree get() {
        final CategoryTree categoryTree = categoryTreeProvider.get();
        return categorySettings.newExtId()
                .flatMap(categoryTree::findByExternalId)
                .map(categoryTree::findChildren)
                .map(categoryTree::getSubtree)
                .orElseGet(() -> CategoryTree.of(emptyList()));
    }
}
