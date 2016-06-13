package com.commercetools.sunrise.common.categorytree;

import com.commercetools.sunrise.common.SunriseInitializationException;
import com.google.inject.Provider;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;
import play.Logger;

import javax.inject.Inject;

public final class CategoryTreeProvider implements Provider<CategoryTree> {

    @Inject
    private SphereClient client;

    @Override
    public CategoryTree get() {
        try {
            final RefreshableCategoryTree categoryTree = RefreshableCategoryTree.of(client);
            Logger.info("Provide RefreshableCategoryTree with " + categoryTree.getAllAsFlatList().size() + " categories");
            return categoryTree;
        } catch (RuntimeException e) {
            throw new SunriseInitializationException("Could not fetch categories", e);
        }
    }
}
