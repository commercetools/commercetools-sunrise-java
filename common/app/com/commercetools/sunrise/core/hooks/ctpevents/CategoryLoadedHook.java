package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;

public interface CategoryLoadedHook extends CtpEventHook {

    void onCategoryLoaded(final Category category);

    static void runHook(final HookRunner hookRunner, final Category category) {
        hookRunner.runEventHook(CategoryLoadedHook.class, hook -> hook.onCategoryLoaded(category));
    }
}

