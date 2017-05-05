package com.commercetools.sunrise.categorytree;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.CategoryTree;

import java.util.concurrent.CompletionStage;

@ImplementedBy(CategoryTreeFilterImpl.class)
public interface CategoryTreeFilter {

    CompletionStage<CategoryTree> filter(final CategoryTree categoryTree);
}
