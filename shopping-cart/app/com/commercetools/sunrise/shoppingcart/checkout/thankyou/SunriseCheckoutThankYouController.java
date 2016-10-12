package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.OrderLoadedHook;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.hooks.requests.OrderByIdGetHook;
import com.commercetools.sunrise.shoppingcart.OrderSessionHandler;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.mvc.Call;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

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
@RequestScoped
public abstract class SunriseCheckoutThankYouController extends SunriseFrameworkShoppingCartController
        implements WithTemplateName {

    @Inject
    private CheckoutThankYouPageContentFactory pageContentFactory;

    @SunriseRoute("checkoutThankYouPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> findLastOrder().
                thenComposeAsync(orderOpt -> orderOpt
                        .map(this::handleFoundOrder)
                        .orElseGet(this::handleNotFoundOrder), defaultContext()));
    }

    protected CompletionStage<Optional<Order>> findLastOrder() {
        return injector().getInstance(OrderSessionHandler.class).findLastOrderId(session())
                .map(this::findOrderById)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected CompletionStage<Optional<Order>> findOrderById(final String orderId) {
        final OrderByIdGet baseRequest = OrderByIdGet.of(orderId).plusExpansionPaths(m -> m.paymentInfo().payments());
        final OrderByIdGet orderByIdGet = OrderByIdGetHook.runHook(hooks(), baseRequest);
        return sphere().execute(orderByIdGet)
                .thenApplyAsync(nullableOrder -> {
                    if (nullableOrder != null) {
                        OrderLoadedHook.runHook(hooks(), nullableOrder);
                    }
                    return Optional.ofNullable(nullableOrder);
                }, defaultContext());
    }

    protected CompletionStage<Result> handleFoundOrder(final Order order) {
        final CheckoutThankYouPageContent pageContent = pageContentFactory.create(order);
        return asyncOk(renderPageWithTemplate(pageContent, getTemplateName()));
    }

    protected CompletionStage<Result> handleNotFoundOrder() {
        final Call call = injector().getInstance(HomeReverseRouter.class).homePageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    @Override
    public String getTemplateName() {
        return "checkout-thankyou";
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("checkout", "checkout-thank-you"));
        return frameworkTags;
    }
}
