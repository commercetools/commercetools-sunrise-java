package com.commercetools.sunrise;

import com.google.inject.AbstractModule;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import java.util.List;

public class CategoryTreeTestModule extends AbstractModule {

    private final List<Category> categories;

    public CategoryTreeTestModule(final List<Category> categories) {
        this.categories = categories;
    }

    @Override
    protected void configure() {
        bind(CategoryTree.class).toInstance(CategoryTree.of(categories));
    }
}
