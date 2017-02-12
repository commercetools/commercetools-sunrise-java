package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.controllers.WithFetchFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.OrderLoadedHook;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.hooks.requests.OrderByIdGetHook;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.view.CheckoutThankYouPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.view.CheckoutThankYouPageContentFactory;
import io.sphere.sdk.orders.Order;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;

/**
 * Controller to show as last checkout step the confirmation of the order data.
 * By default the last order ID is taken from the cookie.
 *
 * <p id="hooks">supported hooks</p>
 * <ul>
 *     <li>{@link RequestStartedHook}</li>
 *     <li>{@link PageDataReadyHook}</li>
 *     <li>{@link OrderByIdGetHook}</li>
 *     <li>{@link OrderLoadedHook}</li>
 * </ul>
 * <p>tags</p>
 * <ul>
 *     <li>checkout-thank-you</li>
 *     <li>checkout</li>
 * </ul>
 */
public abstract class SunriseCheckoutThankYouController extends SunriseFrameworkController implements WithTemplateName, WithFetchFlow<Order> {

    private final OrderFinder orderFinder;
    private final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory;

    protected SunriseCheckoutThankYouController(final OrderFinder orderFinder,
                                                final CheckoutThankYouPageContentFactory checkoutThankYouPageContentFactory) {
        this.orderFinder = orderFinder;
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

    @SunriseRoute("checkoutThankYouPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireOrder(this::showPage));
    }

    protected abstract CompletionStage<Result> handleNotFoundOrder();

    @Override
    public CompletionStage<Content> renderPage(final Order order) {
        final CheckoutThankYouPageContent pageContent = checkoutThankYouPageContentFactory.create(order);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }

    private CompletionStage<Result> requireOrder(final Function<Order, CompletionStage<Result>> nextAction) {
        return orderFinder.get()
                .thenComposeAsync(order -> order
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundOrder),
                        HttpExecution.defaultContext());
    }
}
