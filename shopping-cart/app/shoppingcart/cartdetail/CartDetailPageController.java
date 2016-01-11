package shoppingcart.cartdetail;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.controllers.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.models.Base;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import shoppingcart.common.CartController;
import shoppingcart.CartSessionUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Shows the contents of the cart.
 */
@Singleton
public final class CartDetailPageController extends CartController {
    private final ProductDataConfig productDataConfig;

    @Inject
    public CartDetailPageController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> renderCartPage(cart, userContext));
    }

    public F.Promise<Result> addToCart(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Http.Session session = session();
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session);
        final Form<AddToCartFormData> filledForm = obtainFilledForm();
        if (filledForm.hasErrors()) {
            return F.Promise.pure(badRequest());
        } else {
            final String productId = filledForm.get().getProductId();
            final int variantId = filledForm.get().getVariantId();
            final long quantity = filledForm.get().getQuantity();
            final AddLineItem updateAction = AddLineItem.of(productId, variantId, quantity);
            return cartPromise.flatMap(cart ->
                sphere().execute(CartUpdateCommand.of(cart, updateAction)).map(updatedCart -> {
                    CartSessionUtils.overwriteCartSessionData(updatedCart, session);
                    return renderCartPage(updatedCart, userContext);
                })
            );
        }
    }

    private Result renderCartPage(final Cart cart, final UserContext userContext) {
        final CartDetailPageContent content = new CartDetailPageContent(cart, userContext, productDataConfig, i18nResolver(), reverseRouter());
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return ok(templateService().renderToHtml("cart", pageData, userContext.locales()));
    }

    private Form<AddToCartFormData> obtainFilledForm() {
        return Form.form(AddToCartFormData.class, AddToCartFormData.Validation.class).bindFromRequest(request());
    }

    @RequireCSRFCheck
    public F.Promise<Result> processRemoveLineItem(final String languageTag) {
        final String lineItemId = DynamicForm.form().bindFromRequest().get("lineItemId");
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .flatMap(cart -> sphere().execute(CartUpdateCommand.of(cart, RemoveLineItem.of(lineItemId)))
                .map(updatedCart -> {
                    CartSessionUtils.overwriteCartSessionData(cart, session());
                    return redirect(reverseRouter().showCart(languageTag));
                }));
    }

    @RequireCSRFCheck
    public F.Promise<Result> processChangeLineItemQuantity(final String languageTag) {
        final Form<LineItemQuantityFormData> filledForm = Form.form(LineItemQuantityFormData.class).bindFromRequest();
        if (filledForm.hasErrors()) {
            return F.Promise.pure(redirect(reverseRouter().showCart(languageTag)));
        } else {
            final LineItemQuantityFormData value = filledForm.get();
            final UserContext userContext = userContext(languageTag);
            return getOrCreateCart(userContext, session())
                    .flatMap(cart -> sphere().execute(CartUpdateCommand.of(cart, ChangeLineItemQuantity.of(value.lineItemId, value.quantity)))
                            .map(updatedCart -> {
                                CartSessionUtils.overwriteCartSessionData(cart, session());
                                return redirect(reverseRouter().showCart(languageTag));
                            }));
        }
    }

    public static class LineItemQuantityFormData extends Base {
        @Constraints.Required
        private String lineItemId;
        @Constraints.Min(0)
        @Constraints.Max(100)
        @Constraints.Required
        private Long quantity;

        public String getLineItemId() {
            return lineItemId;
        }

        public void setLineItemId(final String lineItemId) {
            this.lineItemId = lineItemId;
        }

        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(final Long quantity) {
            this.quantity = quantity;
        }
    }
}
