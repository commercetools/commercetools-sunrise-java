package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller to show as last checkout step the confirmation of the order data.
 * By default the last order ID is taken from the cookie.
 */
public abstract class SunriseCheckoutThankYouController extends SunriseContentController implements WithQueryFlow<Void> {

    protected SunriseCheckoutThankYouController(final ContentRenderer contentRenderer) {
        super(contentRenderer);
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_THANK_YOU_PAGE)
    public CompletionStage<Result> show() {
        return showPage(null);
    }

    @Override
    public PageContent createPageContent(final Void input) {
        return new BlankPageContent();
    }
}
