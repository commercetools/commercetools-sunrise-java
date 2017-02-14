package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.controllers.SunriseTemplateController;
import com.commercetools.sunrise.common.controllers.WithQueryFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.view.CheckoutThankYouPageContentFactory;
import io.sphere.sdk.orders.Order;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

/**
 * Controller to show as last checkout step the confirmation of the order data.
 * By default the last order ID is taken from the cookie.
 */
public abstract class SunriseCheckoutThankYouController extends SunriseTemplateController implements WithQueryFlow<Order>, WithRequiredOrderCreated {

    private final OrderCreatedFinder orderCreatedFinder;
    private final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory;

    protected SunriseCheckoutThankYouController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                                final OrderCreatedFinder orderCreatedFinder,
                                                final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory) {
        super(hookContext, templateRenderer);
        this.orderCreatedFinder = orderCreatedFinder;
        this.checkoutThankYouPageContentFactory = checkoutThankYouPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("checkout", "checkout-thank-you"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-thankyou";
    }

    @Override
    public OrderCreatedFinder getOrderCreatedFinder() {
        return orderCreatedFinder;
    }

    @SunriseRoute("checkoutThankYouPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireOrderCreated(this::showPage));
    }

    @Override
    public PageContent createPageContent(final Order order) {
        return checkoutThankYouPageContentFactory.create(order);
    }
}
