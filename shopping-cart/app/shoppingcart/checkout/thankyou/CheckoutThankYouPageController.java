package shoppingcart.checkout.thankyou;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import shoppingcart.OrderSessionUtils;
import shoppingcart.common.CartController;
import shoppingcart.common.CartOrderBean;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class CheckoutThankYouPageController extends CartController {

    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutThankYouPageController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return OrderSessionUtils.getLastOrderId(session())
                .map(lastOrderId -> findOrder(lastOrderId)
                        .thenApplyAsync(orderOpt -> orderOpt
                                .map(order -> handleFoundOrder(order, userContext))
                                .orElseGet(() -> handleNotFoundOrder(userContext)),
                                HttpExecution.defaultContext())
                ).orElseGet(() -> CompletableFuture.completedFuture(handleNotFoundOrder(userContext)));
    }

    protected CompletionStage<Optional<Order>> findOrder(final String lastOrderId) {
        return sphere().execute(OrderByIdGet.of(lastOrderId))
                .thenApplyAsync(Optional::ofNullable, HttpExecution.defaultContext());
    }

    protected Result handleFoundOrder(final Order order, final UserContext userContext) {
        final CheckoutThankYouPageContent pageContent = new CheckoutThankYouPageContent();
        pageContent.setOrder(new CartOrderBean(order, userContext, productDataConfig, reverseRouter()));
        return renderCheckoutThankYouPage(pageContent, userContext);
    }

    protected Result handleNotFoundOrder(final UserContext userContext) {
        return redirect(reverseRouter().showHome(userContext.locale().toLanguageTag()));
    }

    protected Result renderCheckoutThankYouPage(final CheckoutThankYouPageContent pageContent, final UserContext userContext) {
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:thankYouPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return ok(templateService().renderToHtml("checkout-thankyou", pageData, userContext.locales()));
    }
}
