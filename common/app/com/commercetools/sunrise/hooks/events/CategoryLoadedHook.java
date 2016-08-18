package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.categories.Category;

import java.util.concurrent.CompletionStage;

public interface CategoryLoadedHook extends EventHook {

    CompletionStage<?> onCategoryLoaded(final Category category);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Category category) {
        return hookRunner.runEventHook(CategoryLoadedHook.class, hook -> hook.onCategoryLoaded(category));
    }
}

