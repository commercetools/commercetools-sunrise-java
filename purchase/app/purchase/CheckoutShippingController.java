package purchase;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.models.ProductDataConfig;
import common.pages.SunrisePageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.client.PlayJavaSphereClient;
import io.sphere.sdk.models.Address;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class CheckoutShippingController extends CartController {


    private final ShippingMethods shippingMethods;
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutShippingController(final ControllerDependency controllerDependency, final ShippingMethods shippingMethods, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.shippingMethods = shippingMethods;
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final Messages messages = messages(userContext);
            final CheckoutShippingContent content = new CheckoutShippingContent(cart, messages, configuration(), reverseRouter(), userContext, flash(), getCsrfToken(), shippingMethods, productDataConfig);
            final SunrisePageData pageData = pageData(userContext, content);
            return ok(templateService().renderToHtml("checkout-shipping", pageData, userContext.locales()));
        });
    }

    @AddCSRFToken
    @RequireCSRFCheck
    public F.Promise<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final PlayJavaSphereClient sphere = sphere();
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.flatMap(cart -> {
            final Form<CheckoutShippingFormData> filledForm = Form.form(CheckoutShippingFormData.class, CheckoutShippingFormData.Validation.class).bindFromRequest(request());
            final CheckoutShippingFormData checkoutShippingFormData = extractBean(request(), CheckoutShippingFormData.class);
            final Messages messages = messages(userContext);
            final String csrfToken = getCsrfToken();
            final CheckoutShippingContent content = new CheckoutShippingContent(checkoutShippingFormData, cart, messages, configuration(), reverseRouter(), userContext, flash(), csrfToken, shippingMethods, productDataConfig);
            additionalValidations(filledForm, checkoutShippingFormData);
            if (filledForm.hasErrors()) {
                Logger.info("cart not valid");
                final SunrisePageData pageData = pageData(userContext, content);
                return F.Promise.pure(badRequest(templateService().renderToHtml("checkout-shipping", pageData, userContext.locales())));
            } else {
                final Address shippingAddress = content.getShippingForm().getShippingAddress().toAddress();
                final Address nullableBillingAddress = content.getShippingForm().isBillingAddressDifferentToBillingAddress()
                        ? content.getShippingForm().getBillingAddress().toAddress()
                        : null;
                return sphere.execute(CartUpdateCommand.of(cart, asList(SetShippingAddress.of(shippingAddress), SetBillingAddress.of(nullableBillingAddress))))
                        .map(updatedCart -> {
                            Logger.info("cart updated " + updatedCart);
                            final Result redirect = redirect(reverseRouter().showCheckoutShippingForm(languageTag));
                            return redirect;
                        });
            }
        });
    }

    private void additionalValidations(final Form<CheckoutShippingFormData> filledForm, final CheckoutShippingFormData checkoutShippingFormData) {
        if (checkoutShippingFormData.isBillingAddressDifferentToBillingAddress() && isBlank(checkoutShippingFormData.getCountryBilling())) {
            filledForm.reject("countryBilling", "form.required");
        }
    }

    private static <T> T extractBean(final Http.Request request, final Class<T> clazz) {
        return DynamicForm.form(clazz, null).bindFromRequest(request).get();
    }
}