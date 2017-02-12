package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import io.sphere.sdk.carts.Cart;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

@NoCache
public abstract class SunriseFrameworkShoppingCartController extends SunriseFrameworkController {

    private final CartFinder cartFinder;

    protected SunriseFrameworkShoppingCartController(final CartFinder cartFinder) {
        this.cartFinder = cartFinder;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("shopping-cart"));
    }

    protected final CompletionStage<Result> requireNonEmptyCart(final Function<Cart, CompletionStage<Result>> nextAction) {
        return cartFinder.get()
                .thenComposeAsync(cartOpt -> cartOpt
                                .filter(cart -> !cart.getLineItems().isEmpty())
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundCart),
                        HttpExecution.defaultContext());
    }

    protected abstract CompletionStage<Result> handleNotFoundCart();
}
