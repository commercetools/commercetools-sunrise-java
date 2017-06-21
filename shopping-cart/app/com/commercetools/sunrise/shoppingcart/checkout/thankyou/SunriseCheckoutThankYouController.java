package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.framework.controllers.SunriseContentController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels.CheckoutThankYouPageContentFactory;
import io.sphere.sdk.orders.Order;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller to show as last checkout step the confirmation of the order data.
 * By default the last order ID is taken from the cookie.
 */
public abstract class SunriseCheckoutThankYouController extends SunriseContentController implements WithQueryFlow<Order>, WithRequiredOrderCreated {

    private final OrderCreatedFinder orderCreatedFinder;
    private final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory;

    protected SunriseCheckoutThankYouController(final ContentRenderer contentRenderer,
                                                final OrderCreatedFinder orderCreatedFinder,
                                                final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory) {
        super(contentRenderer);
        this.orderCreatedFinder = orderCreatedFinder;
        this.checkoutThankYouPageContentFactory = checkoutThankYouPageContentFactory;
    }

    @Override
    public final OrderCreatedFinder getOrderCreatedFinder() {
        return orderCreatedFinder;
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_THANK_YOU_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireOrderCreated(this::showPage);
    }

    @Override
    public PageContent createPageContent(final Order order) {
        return checkoutThankYouPageContentFactory.create(order);
    }
}
