package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.CategoryTree;

public interface CategoryTreeLoadedHook extends CtpEventHook {

    void onCategoryTreeLoaded(final CategoryTree categoryTree);

    static void runHook(final HookRunner hookRunner, final CategoryTree categoryTree) {
        hookRunner.runEventHook(CategoryTreeLoadedHook.class, hook -> hook.onCategoryTreeLoaded(categoryTree));
    }
}
