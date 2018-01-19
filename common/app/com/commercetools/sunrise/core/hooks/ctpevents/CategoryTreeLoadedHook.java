package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.CategoryTree;

@FunctionalInterface
public interface CategoryTreeLoadedHook extends ConsumerHook {

    void onLoaded(CategoryTree categoryTree);

    static void run(final HookRunner hookRunner, final CategoryTree resource) {
        hookRunner.run(CategoryTreeLoadedHook.class, h -> h.onLoaded(resource));
    }
}
