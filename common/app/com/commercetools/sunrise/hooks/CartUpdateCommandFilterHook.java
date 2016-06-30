package com.commercetools.sunrise.hooks;

import io.sphere.sdk.carts.commands.CartUpdateCommand;

public interface CartUpdateCommandFilterHook extends Hook {
    CartUpdateCommand filterCartUpdateCommand(CartUpdateCommand query);
}
