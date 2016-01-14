package shoppingcart.checkout.address;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import shoppingcart.common.CartController;
import shoppingcart.ErrorsBean;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Singleton
public class CheckoutAddressController extends CartController {
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutAddressController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    public F.Promise<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final F.Promise<Cart> cartPromise = getOrCreateCart(userContext, session());
        return cartPromise.map(cart -> {
            final CheckoutAddressPageContent content = new CheckoutAddressPageContent(cart, i18nResolver(), configuration(), userContext, projectContext(), productDataConfig, reverseRouter());
            final SunrisePageData pageData = pageData(userContext, content, ctx());
            return ok(templateService().renderToHtml("checkout-address", pageData, userContext.locales()));
        });
    }

    @AddCSRFToken
    @RequireCSRFCheck
    public F.Promise<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session()).flatMap(cart -> {
            final CheckoutAddressFormData checkoutAddressFormData = extractBean(request(), CheckoutAddressFormData.class);
            final Form<CheckoutAddressFormData> filledForm = obtainFilledForm(checkoutAddressFormData);
            final CheckoutAddressPageContent content = new CheckoutAddressPageContent(checkoutAddressFormData, cart, i18nResolver(), configuration(), userContext, projectContext(), productDataConfig, reverseRouter());
            if (filledForm.hasErrors()) {
                return F.Promise.pure(badRequest(userContext, filledForm, content));
            } else {
                return updateCart(cart, content).map(updatedCart -> redirect(reverseRouter().showCheckoutShippingForm(languageTag)));
            }
        });
    }

    private F.Promise<Cart> updateCart(final Cart cart, final CheckoutAddressPageContent content) {
        final Address shippingAddress = content.getAddressForm().getShippingAddress().toAddress();
        final Address nullableBillingAddress = content.getAddressForm().isBillingAddressDifferentToBillingAddress()
                ? content.getAddressForm().getBillingAddress().toAddress()
                : null;
        final List<UpdateAction<Cart>> updateActions = asList(
                SetCountry.of(shippingAddress.getCountry()),
                SetShippingAddress.of(shippingAddress),
                SetBillingAddress.of(nullableBillingAddress)
        );
        return sphere().execute(CartUpdateCommand.of(cart, updateActions));
    }

    private Result badRequest(final UserContext userContext, final Form<CheckoutAddressFormData> filledForm, final CheckoutAddressPageContent content) {
        Logger.info("cart not valid");
        content.getAddressForm().setErrors(new ErrorsBean(filledForm));
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return badRequest(templateService().renderToHtml("checkout-address", pageData, userContext.locales()));
    }

    private Form<CheckoutAddressFormData> obtainFilledForm(final CheckoutAddressFormData checkoutAddressFormData) {
        final Form<CheckoutAddressFormData> filledForm = Form.form(CheckoutAddressFormData.class, CheckoutAddressFormData.Validation.class).bindFromRequest(request());
        additionalValidations(filledForm, checkoutAddressFormData);
        return filledForm;
    }

    private void additionalValidations(final Form<CheckoutAddressFormData> filledForm, final CheckoutAddressFormData checkoutAddressFormData) {
        if (checkoutAddressFormData.isBillingAddressDifferentToBillingAddress() && isBlank(checkoutAddressFormData.getCountryBilling())) {
            filledForm.reject("countryBilling", "form.required");
        }
    }

    private static <T> T extractBean(final Http.Request request, final Class<T> clazz) {
        return DynamicForm.form(clazz, null).bindFromRequest(request).get();
    }
}