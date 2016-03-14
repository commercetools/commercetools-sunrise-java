package shoppingcart.cartdetail;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.CartSessionUtils;
import shoppingcart.common.CartController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Shows and modifies the contents of the cart.
 */
@Singleton
public final class CartDetailPageController extends CartController {

    private final ProductDataConfig productDataConfig;
    private final Form<AddProductToCartFormData> addProductToCartForm;
    private final Form<ChangeLineItemQuantityFormData> changeLineItemQuantityForm;
    private final Form<RemoveLineItemFormData> removeLineItemForm;

    @Inject
    public CartDetailPageController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig,
                                    final FormFactory formFactory) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
        this.addProductToCartForm = formFactory.form(AddProductToCartFormData.class);
        this.changeLineItemQuantityForm = formFactory.form(ChangeLineItemQuantityFormData.class);
        this.removeLineItemForm = formFactory.form(RemoveLineItemFormData.class);
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .thenApplyAsync(cart -> ok(renderCartPage(cart, userContext)), HttpExecution.defaultContext());
    }

    public CompletionStage<Result> addProductToCart(final String languageTag) {
        final Form<AddProductToCartFormData> form = addProductToCartForm.bindFromRequest();
        if (form.hasErrors()) {
            return completedFuture(badRequest());
        } else {
            final UserContext userContext = userContext(languageTag);
            final Http.Session session = session();
            return getOrCreateCart(userContext, session)
                    .thenComposeAsync(cart -> addProductToCart(form.get(), cart))
                    .thenApplyAsync(updatedCart -> {
                        CartSessionUtils.overwriteCartSessionData(updatedCart, session, userContext, reverseRouter());
                        return redirect(reverseRouter().showCart(languageTag));
                    }, HttpExecution.defaultContext());
        }
    }

    @RequireCSRFCheck
    public CompletionStage<Result> changeLineItemQuantity(final String languageTag) {
        final Form<ChangeLineItemQuantityFormData> form = changeLineItemQuantityForm.bindFromRequest();
        if (form.hasErrors()) {
            return completedFuture(redirect(reverseRouter().showCart(languageTag)));
        } else {
            final UserContext userContext = userContext(languageTag);
            final Http.Session session = session();
            return getOrCreateCart(userContext, session)
                    .thenComposeAsync(cart -> changeLineItemQuantity(form.get(), cart))
                    .thenApplyAsync(updatedCart -> {
                        CartSessionUtils.overwriteCartSessionData(updatedCart, session, userContext, reverseRouter());
                        return redirect(reverseRouter().showCart(languageTag));
                    }, HttpExecution.defaultContext());
        }
    }

    @RequireCSRFCheck
    public CompletionStage<Result> removeLineItem(final String languageTag) {
        final Form<RemoveLineItemFormData> form = removeLineItemForm.bindFromRequest();
        if (form.hasErrors()) {
            return completedFuture(badRequest());
        } else {
            final UserContext userContext = userContext(languageTag);
            return getOrCreateCart(userContext, session())
                    .thenComposeAsync(cart -> removeLineItem(form.get(), cart))
                    .thenApplyAsync(updatedCart -> {
                        CartSessionUtils.overwriteCartSessionData(updatedCart, session(), userContext, reverseRouter());
                        return redirect(reverseRouter().showCart(languageTag));
                    }, HttpExecution.defaultContext());
        }
    }

    private CompletionStage<Cart> addProductToCart(final AddProductToCartFormData formData, final Cart cart) {
        final String productId = formData.getProductId();
        final int variantId = formData.getVariantId();
        final long quantity = formData.getQuantity();
        final AddLineItem updateAction = AddLineItem.of(productId, variantId, quantity);
        return sphere().execute(CartUpdateCommand.of(cart, updateAction));
    }

    private CompletionStage<Cart> changeLineItemQuantity(final ChangeLineItemQuantityFormData formData, final Cart cart) {
        final String lineItemId = formData.getLineItemId();
        final Long quantity = formData.getQuantity();
        return sphere().execute(CartUpdateCommand.of(cart, ChangeLineItemQuantity.of(lineItemId, quantity)));
    }

    private CompletionStage<Cart> removeLineItem(final RemoveLineItemFormData formData, final Cart cart) {
        final String lineItemId = formData.getLineItemId();
        return sphere().execute(CartUpdateCommand.of(cart, RemoveLineItem.of(lineItemId)));
    }

    private Html renderCartPage(final Cart cart, final UserContext userContext) {
        final CartDetailPageContent content = new CartDetailPageContent(cart, userContext, productDataConfig, i18nResolver(), reverseRouter());
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return templateService().renderToHtml("cart", pageData, userContext.locales());
    }
}
