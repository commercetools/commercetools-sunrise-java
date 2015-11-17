package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.pages.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.json.SphereJsonUtils;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

import static java.util.Arrays.asList;

/**
 * Shows the contents of the cart.
 */
public final class CartDetailPageController extends CartController {
    private final ProductDataConfig productDataConfig;

    @Inject
    public CartDetailPageController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> setItemsToCart(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Http.Session session = session();
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session);
        return cartPromise.flatMap(cart -> sphere().execute(CartUpdateCommand.of(cart,
                asList(AddLineItem.of("421f1414-1b30-46eb-821d-d0f2d10f8135", 1, 5), AddLineItem.of("766936c7-f525-42ab-851d-f6b40a7bfa20", 1, 3)))))
        .map(cart -> {
            CartSessionUtils.overwriteCartSessionData(cart, session);
            return ok(SphereJsonUtils.toJsonNode(cart));
        });
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final Messages messages = messages(userContext);
            final CartDetailPageContent content = new CartDetailPageContent(cart, userContext, messages, reverseRouter(), productDataConfig);
            final SunrisePageData pageData = pageData(userContext, content);
            return ok(templateService().renderToHtml("cart", pageData, userContext.locales()));
        });
    }
}
