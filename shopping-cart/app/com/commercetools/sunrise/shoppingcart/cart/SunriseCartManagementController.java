package com.commercetools.sunrise.shoppingcart.cart;

import com.commercetools.sunrise.common.reverserouter.CartSimpleReverseRouter;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import play.mvc.Call;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseCartManagementController extends SunriseFrameworkShoppingCartController {

    protected final CompletionStage<Result> redirectToCartDetail() {
        final Call call = injector().getInstance(CartSimpleReverseRouter.class).showCart(userContext().languageTag());
        return completedFuture(redirect(call));
    }
}
