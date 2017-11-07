package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import com.google.inject.Inject;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddDiscountCode;
import io.sphere.sdk.client.SphereClient;

import java.util.concurrent.CompletionStage;

public class DefaultAddDiscountCodeControllerAction extends AbstractCartUpdateExecutor implements AddDiscountCodeControllerAction {

    @Inject
    protected DefaultAddDiscountCodeControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final AddDiscountCodeFormData addDiscountCodeFormData) {
        return executeRequest(cart, buildRequest(cart, addDiscountCodeFormData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final AddDiscountCodeFormData addDiscountCodeFormData) {
        final AddDiscountCode addDiscountCode = AddDiscountCode.of(addDiscountCodeFormData.code());
        return CartUpdateCommand.of(cart, addDiscountCode);
    }
}
