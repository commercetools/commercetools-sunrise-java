package com.commercetools.sunrise.framework.hooks.events;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.models.Address;

import java.util.concurrent.CompletionStage;

public interface AddressLoadedHook extends EventHook {

    CompletionStage<?> onAddressLoaded(final Address address);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Address address) {
        return hookRunner.runEventHook(AddressLoadedHook.class, hook -> hook.onAddressLoaded(address));
    }
}
