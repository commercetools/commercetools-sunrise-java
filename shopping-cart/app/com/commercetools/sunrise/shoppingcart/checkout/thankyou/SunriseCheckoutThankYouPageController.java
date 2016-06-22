package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.hooks.OrderByIdGetFilterHook;
import com.commercetools.sunrise.hooks.SingleOrderHook;
import com.commercetools.sunrise.shoppingcart.OrderSessionUtils;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@Singleton
public abstract class SunriseCheckoutThankYouPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {

    @Inject
    private HomeReverseRouter homeReverseRouter;

    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            return getLastOrderId()
                    .map(lastOrderId -> findOrder(lastOrderId)
                            .thenComposeAsync(orderOpt -> orderOpt
                                    .map(order -> handleFoundOrder(order))
                                    .orElseGet(() -> handleNotFoundOrder()),
                                    defaultContext())
                    )
                    .orElseGet(() -> handleNotFoundOrder());
        });
    }

    protected Optional<String> getLastOrderId() {
        return OrderSessionUtils.getLastOrderId(session());
    }

    protected CompletionStage<Optional<Order>> findOrder(final String orderId) {
        final OrderByIdGet orderByIdGet = runFilterHook(OrderByIdGetFilterHook.class, (hook, getter) -> hook.filterOrderByIdGet(getter), OrderByIdGet.of(orderId));
        return sphere().execute(orderByIdGet)
                .thenApplyAsync(nullableOrder -> {
                    if (nullableOrder != null) {
                        runAsyncHook(SingleOrderHook.class, hook -> hook.onSingleOrderLoaded(nullableOrder));
                    }
                    return nullableOrder;
                }, defaultContext())
                .thenApplyAsync(Optional::ofNullable, defaultContext());
    }

    protected CompletionStage<Result> handleFoundOrder(final Order order) {
        final CheckoutThankYouPageContent pageContent = new CheckoutThankYouPageContent();
        pageContent.setOrder(cartLikeBeanFactory.create(order));
        return asyncOk(renderCheckoutThankYouPage(pageContent));
    }

    protected CompletionStage<Result> handleNotFoundOrder() {
        final Call call = homeReverseRouter.homePageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Html> renderCheckoutThankYouPage(final CheckoutThankYouPageContent pageContent) {
        setI18nTitle(pageContent, "checkout:thankYouPage.title");
        return renderPage(pageContent, getTemplateName());
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
