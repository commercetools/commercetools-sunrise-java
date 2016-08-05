package com.commercetools.sunrise.hooks.consumers;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.hooks.HookRunner;

public interface PageDataHook extends ConsumerHook {

    void onPageDataCreated(final PageData pageData);

    static void runHook(final HookRunner hookRunner, final PageData pageData) {
        hookRunner.runConsumerHook(PageDataHook.class, hook -> hook.onPageDataCreated(pageData));
    }
}
