package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import com.google.inject.Inject;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.RemoveDiscountCode;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.discountcodes.DiscountCode;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveDiscountCodeControllerAction extends AbstractCartUpdateExecutor implements RemoveDiscountCodeControllerAction {

    @Inject
    protected DefaultRemoveDiscountCodeControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final RemoveDiscountCodeFormData removeDiscountCodeFormData) {
        return executeRequest(cart, buildRequest(cart, removeDiscountCodeFormData));
    }

    protected CartUpdateCommand buildRequest(final Cart cart, final RemoveDiscountCodeFormData removeDiscountCodeFormData) {
        final RemoveDiscountCode addDiscountCode = RemoveDiscountCode.of(DiscountCode.referenceOfId(removeDiscountCodeFormData.discountCodeId()));
        return CartUpdateCommand.of(cart, Arrays.asList(addDiscountCode));
    }
}
