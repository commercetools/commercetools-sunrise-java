package com.commercetools.sunrise.core.hooks.application;

import com.commercetools.sunrise.core.viewmodels.OldPageData;
import com.commercetools.sunrise.core.hooks.HookRunner;

/**
 * Hook to be executed right after the {@link OldPageData} has been generated and before transforming it into some readable content, such as HTML.
 */
public interface PageDataReadyHook extends ApplicationHook {

    void onPageDataReady(final OldPageData oldPageData);

    static void runHook(final HookRunner hookRunner, final OldPageData oldPageData) {
        hookRunner.runConsumerHook(PageDataReadyHook.class, hook -> hook.onPageDataReady(oldPageData));
    }
}
