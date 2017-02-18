package com.commercetools.sunrise.framework.hooks.consumers;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.hooks.HookRunner;

public interface PageDataReadyHook extends ConsumerHook {

    void onPageDataReady(final PageData pageData);

    static void runHook(final HookRunner hookRunner, final PageData pageData) {
        hookRunner.runConsumerHook(PageDataReadyHook.class, hook -> hook.onPageDataReady(pageData));
    }
}
