package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.hooks.OrderByIdGetFilterHook;
import com.commercetools.sunrise.hooks.RequestHook;
import com.commercetools.sunrise.hooks.SingleOrderHook;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.shoppingcart.OrderSessionUtils;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.google.inject.Injector;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.mvc.Call;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
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
 *     <li>{@link RequestHook}</li>
 *     <li>{@link SunrisePageDataHook}</li>
 *     <li>{@link OrderByIdGetFilterHook}</li>
 *     <li>{@link SingleOrderHook}</li>
 * </ul>
 * <p>tags</p>
 * <ul>
 *     <li>checkout-thankyou</li>
 *     <li>checkout</li>
 * </ul>
 */
@RequestScoped
public abstract class SunriseCheckoutThankYouPageController extends SunriseFrameworkCartController
        implements WithOverwriteableTemplateName {

    @Inject
    private Injector injector;
    @Inject
    private CheckoutThankYouPageContentFactory pageContentFactory;

    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> findLastOrder().
                thenComposeAsync(orderOpt -> orderOpt
                        .map(this::handleFoundOrder)
                        .orElseGet(this::handleNotFoundOrder), defaultContext()));
    }

    protected CompletionStage<Optional<Order>> findLastOrder() {
        final Optional<String> lastOrderId = OrderSessionUtils.getLastOrderId(session());
        return lastOrderId
                .map(orderId -> findOrderById(orderId))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected CompletionStage<Optional<Order>> findOrderById(final String orderId) {
        final OrderByIdGet orderByIdGet = hooks().runFilterHook(OrderByIdGetFilterHook.class, (hook, getter) -> hook.filterOrderByIdGet(getter), OrderByIdGet.of(orderId));
        return sphere().execute(orderByIdGet)
                .thenApplyAsync(nullableOrder -> {
                    if (nullableOrder != null) {
                        hooks().runAsyncHook(SingleOrderHook.class, hook -> hook.onSingleOrderLoaded(nullableOrder));
                    }
                    return Optional.ofNullable(nullableOrder);
                }, defaultContext());
    }

    protected CompletionStage<Result> handleFoundOrder(final Order order) {
        final CheckoutThankYouPageContent pageContent = pageContentFactory.create(order);
        return asyncOk(renderPage(pageContent, getTemplateName()));
    }

    protected CompletionStage<Result> handleNotFoundOrder() {
        final Call call = injector.getInstance(HomeReverseRouter.class).homePageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    @Override
    public String getTemplateName() {
        return "checkout-thankyou";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-thankyou"));
    }
}
