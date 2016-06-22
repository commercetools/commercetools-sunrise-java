package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.OrderSessionUtils;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.concurrent.HttpExecution;
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

@Singleton
public abstract class SunriseCheckoutThankYouPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {

    @Inject
    private HomeReverseRouter homeReverseRouter;

    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            return OrderSessionUtils.getLastOrderId(session())
                    .map(lastOrderId -> findOrder(lastOrderId)
                            .thenComposeAsync(orderOpt -> orderOpt
                                    .map(order -> handleFoundOrder(order, userContext()))
                                    .orElseGet(() -> handleNotFoundOrder(userContext())),
                                    HttpExecution.defaultContext())
                    )
                    .orElseGet(() -> handleNotFoundOrder(userContext()));
        });
    }

    protected CompletionStage<Optional<Order>> findOrder(final String lastOrderId) {
        return sphere().execute(OrderByIdGet.of(lastOrderId))
                .thenApplyAsync(Optional::ofNullable, HttpExecution.defaultContext());
    }

    protected CompletionStage<Result> handleFoundOrder(final Order order, final UserContext userContext) {
        final CheckoutThankYouPageContent pageContent = new CheckoutThankYouPageContent();
        pageContent.setOrder(createCartLikeBean(order, userContext));
        return asyncOk(renderCheckoutThankYouPage(pageContent));
    }

    protected CompletionStage<Result> handleNotFoundOrder(final UserContext userContext) {
        final Call call = homeReverseRouter.homePageCall(userContext.locale().toLanguageTag());
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
