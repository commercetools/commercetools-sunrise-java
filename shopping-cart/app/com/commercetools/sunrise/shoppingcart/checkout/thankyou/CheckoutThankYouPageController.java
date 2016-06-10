package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ControllerDependency;
import com.commercetools.sunrise.common.controllers.SunrisePageData;
import com.commercetools.sunrise.common.models.ProductDataConfig;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;
import com.commercetools.sunrise.shoppingcart.OrderSessionUtils;
import com.commercetools.sunrise.shoppingcart.common.CartController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
public class CheckoutThankYouPageController extends CartController {

    @Inject
    public CheckoutThankYouPageController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency, productDataConfig);
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return OrderSessionUtils.getLastOrderId(session())
                .map(lastOrderId -> findOrder(lastOrderId)
                        .thenComposeAsync(orderOpt -> orderOpt
                                .map(order -> handleFoundOrder(order, userContext))
                                .orElseGet(() -> handleNotFoundOrder(userContext)),
                                HttpExecution.defaultContext())
                )
                .orElseGet(() -> handleNotFoundOrder(userContext));
    }

    protected CompletionStage<Optional<Order>> findOrder(final String lastOrderId) {
        return sphere().execute(OrderByIdGet.of(lastOrderId))
                .thenApplyAsync(Optional::ofNullable, HttpExecution.defaultContext());
    }

    protected CompletionStage<Result> handleFoundOrder(final Order order, final UserContext userContext) {
        final CheckoutThankYouPageContent pageContent = new CheckoutThankYouPageContent();
        pageContent.setOrder(createCartLikeBean(order, userContext));
        return completedFuture(ok(renderCheckoutThankYouPage(pageContent, userContext)));
    }

    protected CompletionStage<Result> handleNotFoundOrder(final UserContext userContext) {
        final Call call = reverseRouter().homePageCall(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected Html renderCheckoutThankYouPage(final CheckoutThankYouPageContent pageContent, final UserContext userContext) {
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:thankYouPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("checkout-thankyou", pageData, userContext.locales());
    }
}
