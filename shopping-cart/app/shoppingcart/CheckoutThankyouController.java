package shoppingcart;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static shoppingcart.CartSessionKeys.LAST_ORDER_ID_KEY;

@Singleton
public class CheckoutThankyouController extends CartController {
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutThankyouController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final String lastOrderId = session(LAST_ORDER_ID_KEY);
        return isBlank(lastOrderId) ? F.Promise.pure(redirectToCart(languageTag)) : loadOrder(lastOrderId, languageTag);
    }

    private Result redirectToCart(final String languageTag) {
        return redirect(reverseRouter().showCart(languageTag));
    }

    private F.Promise<Result> loadOrder(final String lastOrderId, final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Http.Context ctx = ctx();
        return sphere().execute(OrderByIdGet.of(lastOrderId))
                .map(order -> {
                    final CheckoutThankYouContent content = new CheckoutThankYouContent(order, productDataConfig, userContext, reverseRouter(), i18nResolver());
                    final SunrisePageData pageData = pageData(userContext, content, ctx);
                    return ok(templateService().renderToHtml("checkout-thankyou", pageData, userContext.locales()));
                });
    }
}