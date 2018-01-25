package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.CategoryTree;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCategoryTreeFetcher.class)
public interface CategoryTreeFetcher extends ResourceFetcher {

    CompletionStage<CategoryTree> get();
}
