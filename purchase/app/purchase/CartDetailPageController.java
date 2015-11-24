package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.controllers.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.data.DynamicForm;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

        final F.Promise<List<UpdateAction<Cart>>> updateActionsPromise = sphere().execute(ProductProjectionQuery.ofCurrent()
                .withPredicates(m -> m.slug().lang(Locale.ENGLISH).isIn(asList("dondup-jeans-george-UP232DS107UG61-blue", "mu-shirt-david-F10216-lightblue"))))
                .map(PagedQueryResult::getResults)
                .map(products -> products.stream().map(product -> AddLineItem.of(product.getId(), 1, 3)).collect(Collectors.toList()));

        final F.Promise<Cart> updatedCart = cartPromise.flatMap(cart ->
                updateActionsPromise.flatMap(updateActions ->
                        sphere().execute(CartUpdateCommand.of(cart, updateActions))
                ));


        return updatedCart.map(cart -> {
            CartSessionUtils.overwriteCartSessionData(cart, session);
            return ok(SphereJsonUtils.toJsonNode(cart));
        });
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final Messages messages = messages(userContext);
            final CartDetailPageContent content = new CartDetailPageContent(cart, userContext, productDataConfig);
            final SunrisePageData pageData = pageData(userContext, content, ctx());
            return ok(templateService().renderToHtml("cart", pageData, userContext.locales()));
        });
    }

    public F.Promise<Result> removeLineItem(final String languageTag) {
        final String lineItemId = DynamicForm.form().bindFromRequest().get("lineItemId");
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .flatMap(cart -> sphere().execute(CartUpdateCommand.of(cart, RemoveLineItem.of(lineItemId)))
                .map(updatedCart -> {
                    CartSessionUtils.overwriteCartSessionData(cart, session());
                    return redirect(reverseRouter().showCart(languageTag));
                }));
    }
}
