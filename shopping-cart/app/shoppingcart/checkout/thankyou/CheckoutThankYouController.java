package shoppingcart.checkout.thankyou;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;
import shoppingcart.common.CartController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static shoppingcart.CartSessionKeys.LAST_ORDER_ID_KEY;

@Singleton
public class CheckoutThankYouController extends CartController {
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutThankYouController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public CompletionStage<Result> show(final String languageTag) {
        final String lastOrderId = session(LAST_ORDER_ID_KEY);
        return isBlank(lastOrderId) ? CompletableFuture.completedFuture(redirectToCart(languageTag)) : loadOrder(lastOrderId, languageTag);
    }

    private Result redirectToCart(final String languageTag) {
        return redirect(reverseRouter().showCart(languageTag));
    }

    private CompletionStage<Result> loadOrder(final String lastOrderId, final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Http.Context ctx = ctx();
        return sphere().execute(OrderByIdGet.of(lastOrderId))
                .thenApplyAsync(order -> {
                    final CheckoutThankYouContent content = new CheckoutThankYouContent(order, userContext, productDataConfig, i18nResolver(), reverseRouter());
                    final SunrisePageData pageData = pageData(userContext, content, ctx);
                    return ok(templateService().renderToHtml("checkout-thankyou", pageData, userContext.locales()));
                }, HttpExecution.defaultContext());
    }
}
