package com.commercetools.sunrise.core.hooks.application;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.viewmodels.PageData;

import java.util.concurrent.CompletionStage;

/**
 * Hook to be executed right after the {@link PageData} has been generated and before transforming it into some readable content, such as HTML.
 */
public interface PageDataHook extends ApplicationHook {

    CompletionStage<PageData> onPageDataReady(final PageData pageData);

    static CompletionStage<PageData> runHook(final HookRunner hookRunner, final PageData pageData) {
        return hookRunner.runActionHook(PageDataHook.class, PageDataHook::onPageDataReady, pageData);
    }
}
