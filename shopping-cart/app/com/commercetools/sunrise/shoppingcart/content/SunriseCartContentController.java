package com.commercetools.sunrise.shoppingcart.content;

import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.models.carts.CartFetcher;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCartContentController extends SunriseContentController implements WithQueryFlow<Cart>, WithRequiredCart {

    private final CartFetcher cartFetcher;
    private final CartPageContentFactory cartPageContentFactory;

    protected SunriseCartContentController(final ContentRenderer contentRenderer,
                                           final CartFetcher cartFetcher,
                                           final CartPageContentFactory cartPageContentFactory) {
        super(contentRenderer);
        this.cartFetcher = cartFetcher;
        this.cartPageContentFactory = cartPageContentFactory;
    }

    @Override
    public final CartFetcher getCartFetcher() {
        return cartFetcher;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.CART_DETAIL_PAGE)
    public CompletionStage<Result> show() {
        return requireCart(this::showPage);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return okResultWithPageContent(cartPageContentFactory.create(null));
    }

    @Override
    public PageContent createPageContent(final Cart cart) {
        return cartPageContentFactory.create(cart);
    }
}
