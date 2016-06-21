package com.commercetools.sunrise.common.categorytree;

import com.google.inject.Provider;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class CategoryTreeInNewProvider implements Provider<CategoryTree> {

    private static final Logger logger = LoggerFactory.getLogger(CategoryTreeInNewProvider.class);
    private static final String CONFIG_CATEGORY_NEW_EXT_ID = "common.newCategoryExternalId";

    @Inject
    private CategoryTree categoryTree;
    @Inject
    private Configuration configuration;

    @Override
    public CategoryTree get() {
        final List<Category> categories = getCategoryNew()
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
        final CategoryTree categoryTreeInNew = categoryTree.getSubtree(categories);
        logger.info("Provide CategoryTreeInNew with " + categoryTreeInNew.getAllAsFlatList().size() + " categories");
        return categoryTreeInNew;
    }

    private Optional<Category> getCategoryNew() {
        return Optional.ofNullable(configuration.getString(CONFIG_CATEGORY_NEW_EXT_ID))
                .flatMap(extId -> categoryTree.findByExternalId(extId));
    }
}
