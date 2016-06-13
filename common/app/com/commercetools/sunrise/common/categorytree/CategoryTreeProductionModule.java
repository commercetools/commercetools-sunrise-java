package com.commercetools.sunrise.common.categorytree;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Singleton;

public class CategoryTreeProductionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CategoryTree.class).toProvider(CategoryTreeProvider.class).in(Singleton.class);
        bind(CategoryTree.class).annotatedWith(Names.named("new")).toProvider(CategoryTreeInNewProvider.class).in(Singleton.class);
    }
}
