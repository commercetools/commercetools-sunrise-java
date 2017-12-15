package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.models.Address;

import java.util.concurrent.CompletionStage;

public interface AddressLoadedHook extends CtpEventHook {

    CompletionStage<?> onAddressLoaded(final Address address);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Address address) {
        return hookRunner.runEventHook(AddressLoadedHook.class, hook -> hook.onAddressLoaded(address));
    }
}
