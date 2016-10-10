package com.commercetools.sunrise.common.categorytree;

import com.google.inject.Provider;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.SphereClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;

public final class RefreshableCategoryTreeProvider implements Provider<CategoryTree> {
    private static final Logger logger = LoggerFactory.getLogger(RefreshableCategoryTreeProvider.class);

    @Inject
    @Named("global")
    private SphereClient client;

    @Override
    public CategoryTree get() {
        final RefreshableCategoryTree categoryTree = RefreshableCategoryTree.of(client);
        logger.info("Provide RefreshableCategoryTree with " + categoryTree.getAllAsFlatList().size() + " categories");
        return categoryTree;
    }
}
