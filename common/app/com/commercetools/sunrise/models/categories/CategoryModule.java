package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.google.inject.AbstractModule;
import io.sphere.sdk.categories.CategoryTree;

public final class CategoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CategoryTree.class)
                .toProvider(CachedCategoryTreeProvider.class);

        bind(CategoryTree.class)
                .annotatedWith(NavigationCategoryTree.class)
                .toProvider(NavigationCategoryTreeProvider.class)
                .in(RequestScoped.class);

        bind(CategoryTree.class)
                .annotatedWith(NewCategoryTree.class)
                .toProvider(NewCategoryTreeProvider.class)
                .in(RequestScoped.class);
    }
}
