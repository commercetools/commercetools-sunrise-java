package com.commercetools.sunrise.framework.hooks.application;

import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.hooks.HookRunner;

/**
 * Hook to be executed right after the {@link PageData} has been generated and before transforming it into some readable content, such as HTML.
 */
public interface PageDataReadyHook extends ApplicationHook {

    void onPageDataReady(final PageData pageData);

    static void runHook(final HookRunner hookRunner, final PageData pageData) {
        hookRunner.runConsumerHook(PageDataReadyHook.class, hook -> hook.onPageDataReady(pageData));
    }
}
