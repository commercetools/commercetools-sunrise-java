package com.commercetools.sunrise.framework.hooks;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;

@ImplementedBy(HookContextImpl.class)
public interface RequestHookRunner extends HookRunner {

    CompletionStage<?> waitForHookedComponentsToFinish();
}
