package com.commercetools.sunrise.productcatalog.hooks;

import com.commercetools.sunrise.common.hooks.Hook;
import io.sphere.sdk.categories.Category;

import java.util.concurrent.CompletionStage;

public interface SingleCategoryHook extends Hook {
    CompletionStage<?> onSingleCategoryLoaded(final Category category);
}

