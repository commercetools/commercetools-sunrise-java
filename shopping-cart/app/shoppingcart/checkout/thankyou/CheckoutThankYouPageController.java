package shoppingcart.checkout.thankyou;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.OrderSessionUtils;
import shoppingcart.common.CartController;

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
        final Call call = reverseRouter().showHome(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected Html renderCheckoutThankYouPage(final CheckoutThankYouPageContent pageContent, final UserContext userContext) {
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:thankYouPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("checkout-thankyou", pageData, userContext.locales());
    }
}
