package com.commercetools.sunrise.shoppingcart.content;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.content.viewmodels.CartPageContentFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCartContentController extends SunriseContentController implements WithQueryFlow<Void> {

    private final CartPageContentFactory cartPageContentFactory;

    protected SunriseCartContentController(final ContentRenderer contentRenderer,
                                           final CartPageContentFactory cartPageContentFactory) {
        super(contentRenderer);
        this.cartPageContentFactory = cartPageContentFactory;
    }

    @EnableHooks
    @SunriseRoute(CartReverseRouter.CART_DETAIL_PAGE)
    public CompletionStage<Result> show() {
        return showPage(null);
    }

    @Override
    public PageContent createPageContent(final Void input) {
        return cartPageContentFactory.create(null);
    }
}
