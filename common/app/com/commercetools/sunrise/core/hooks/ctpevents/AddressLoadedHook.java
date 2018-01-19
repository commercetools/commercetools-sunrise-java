package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.models.Address;

@FunctionalInterface
public interface AddressLoadedHook extends ConsumerHook<Address> {

    void onLoaded(Address address);

    static void run(final HookRunner hookRunner, final Address address) {
        hookRunner.run(AddressLoadedHook.class, h -> h.onLoaded(address));
    }
}
