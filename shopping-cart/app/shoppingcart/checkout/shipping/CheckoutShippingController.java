package shoppingcart.checkout.shipping;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;
import shoppingcart.common.CartController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class CheckoutShippingController extends CartController {

    private final ShippingMethods shippingMethods;
    private final ProductDataConfig productDataConfig;
    private final FormFactory formFactory;

    @Inject
    public CheckoutShippingController(final ControllerDependency controllerDependency, final ShippingMethods shippingMethods,
                                      final ProductDataConfig productDataConfig, final FormFactory formFactory) {
        super(controllerDependency);
        this.shippingMethods = shippingMethods;
        this.productDataConfig = productDataConfig;
        this.formFactory = formFactory;
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final CompletionStage<Cart> cartStage = getOrCreateCart(userContext, session());
        return cartStage.thenApplyAsync(cart -> renderCheckoutShippingForm(userContext, cart), HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Http.Request request = request();
        return getOrCreateCart(userContext, session()).thenComposeAsync(cart -> {
            final CheckoutShippingFormData checkoutShippingFormData = extractBean(request, CheckoutShippingFormData.class);
            final Form<CheckoutShippingFormData> filledForm = obtainFilledForm(request);
            final CheckoutShippingPageContent content = new CheckoutShippingPageContent(cart, checkoutShippingFormData, shippingMethods, userContext, productDataConfig, i18nResolver(), reverseRouter());
            if (filledForm.hasErrors()) {
                return CompletableFuture.completedFuture(badRequest(content, filledForm, userContext));
            } else {
                return updateCart(cart, checkoutShippingFormData)
                        .thenApplyAsync(updatedCart -> redirect(reverseRouter().showCheckoutPaymentForm(languageTag)), HttpExecution.defaultContext());
            }
        }, HttpExecution.defaultContext());
    }

    private CompletionStage<Cart> updateCart(final Cart cart, final CheckoutShippingFormData checkoutShippingFormData) {
        final String shippingMethodId = checkoutShippingFormData.getShippingMethodId();
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(Reference.of(ShippingMethod.referenceTypeId(), shippingMethodId));
        return sphere().execute(CartUpdateCommand.of(cart, setShippingMethod));
    }

    private Result renderCheckoutShippingForm(final UserContext userContext, final Cart cart) {
        final CheckoutShippingPageContent content = new CheckoutShippingPageContent(cart, shippingMethods, productDataConfig, userContext, i18nResolver(), reverseRouter());
        final SunrisePageData pageData = pageData(userContext, content, ctx(), session());
        return ok(templateService().renderToHtml("checkout-shipping", pageData, userContext.locales()));
    }

    private Result badRequest(final CheckoutShippingPageContent content, final Form<CheckoutShippingFormData> filledForm, final UserContext userContext) {
        Logger.info("cart not valid");
        content.getShippingForm().setErrors(new ErrorsBean(filledForm));
        final SunrisePageData pageData = pageData(userContext, content, ctx(), session());
        return badRequest(templateService().renderToHtml("checkout-shipping", pageData, userContext.locales()));
    }

    private Form<CheckoutShippingFormData> obtainFilledForm(final Http.Request request) {
        return formFactory.form(CheckoutShippingFormData.class, CheckoutShippingFormData.Validation.class).bindFromRequest(request);
    }

    private <T> T extractBean(final Http.Request request, final Class<T> clazz) {
        return formFactory.form(clazz, null).bindFromRequest(request).get();
    }
}