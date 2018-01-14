package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.models.Address;

public interface AddressLoadedHook extends CtpEventHook {

    void onAddressLoaded(final Address address);

    static void runHook(final HookRunner hookRunner, final Address address) {
        hookRunner.runEventHook(AddressLoadedHook.class, hook -> hook.onAddressLoaded(address));
    }
}
