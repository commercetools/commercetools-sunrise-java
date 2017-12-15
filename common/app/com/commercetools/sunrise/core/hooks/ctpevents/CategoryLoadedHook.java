package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;

import java.util.concurrent.CompletionStage;

public interface CategoryLoadedHook extends CtpEventHook {

    CompletionStage<?> onCategoryLoaded(final Category category);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Category category) {
        return hookRunner.runEventHook(CategoryLoadedHook.class, hook -> hook.onCategoryLoaded(category));
    }
}

