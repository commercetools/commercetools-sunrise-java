package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.controllers.SunriseTemplateController;
import com.commercetools.sunrise.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.view.CheckoutThankYouPageContentFactory;
import io.sphere.sdk.orders.Order;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Controller to show as last checkout step the confirmation of the order data.
 * By default the last order ID is taken from the cookie.
 */
public abstract class SunriseCheckoutThankYouController extends SunriseTemplateController implements WithQueryFlow<Order>, WithRequiredOrderCreated {

    private final OrderCreatedFinder orderCreatedFinder;
    private final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory;

    protected SunriseCheckoutThankYouController(final TemplateRenderer templateRenderer,
                                                final OrderCreatedFinder orderCreatedFinder,
                                                final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory) {
        super(templateRenderer);
        this.orderCreatedFinder = orderCreatedFinder;
        this.checkoutThankYouPageContentFactory = checkoutThankYouPageContentFactory;
    }

    @Override
    public OrderCreatedFinder getOrderCreatedFinder() {
        return orderCreatedFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_THANK_YOU_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireOrderCreated(this::showPage);
    }

    @Override
    public PageContent createPageContent(final Order order) {
        return checkoutThankYouPageContentFactory.create(order);
    }
}
