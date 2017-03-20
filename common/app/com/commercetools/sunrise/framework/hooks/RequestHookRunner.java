package com.commercetools.sunrise.framework.hooks;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(HookContextImpl.class)
public interface RequestHookRunner extends HookRunner {

    /**
     * Waits for all the asynchronous executions from implemented hooks to finish.
     * @return an unused value
     * @see HookRunner#runEventHook(Class, Function)
     */
    CompletionStage<?> waitForHookedComponentsToFinish();
}
