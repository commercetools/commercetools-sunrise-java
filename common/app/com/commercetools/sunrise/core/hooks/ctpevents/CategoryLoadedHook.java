package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;

@FunctionalInterface
public interface CategoryLoadedHook extends ConsumerHook {

    void onLoaded(Category category);

    static void run(final HookRunner hookRunner, final Category resource) {
        hookRunner.run(CategoryLoadedHook.class, h -> h.onLoaded(resource));
    }
}

