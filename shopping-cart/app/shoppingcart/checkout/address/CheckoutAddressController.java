package shoppingcart.checkout.address;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Singleton
public class CheckoutAddressController extends CartController {
    private final ProductDataConfig productDataConfig;
    private final FormFactory formFactory;

    @Inject
    public CheckoutAddressController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig,
                                     final FormFactory formFactory) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
        this.formFactory = formFactory;
    }

    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .thenApplyAsync(cart -> renderCheckoutAddressPage(userContext, cart), HttpExecution.defaultContext());
    }

    @AddCSRFToken
    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session()).thenComposeAsync(cart -> {
            final CheckoutAddressFormData checkoutAddressFormData = extractBean(request(), CheckoutAddressFormData.class);
            final Form<CheckoutAddressFormData> filledForm = obtainFilledForm(checkoutAddressFormData);
            final CheckoutAddressPageContent content = new CheckoutAddressPageContent(checkoutAddressFormData, cart, i18nResolver(), configuration(), userContext, projectContext(), productDataConfig, reverseRouter());
            if (filledForm.hasErrors()) {
                return CompletableFuture.completedFuture(badRequest(userContext, filledForm, content));
            } else {
                return updateCart(cart, content).thenApplyAsync(updatedCart -> redirect(reverseRouter().showCheckoutShippingForm(languageTag)), HttpExecution.defaultContext());
            }
        });
    }

    private CompletionStage<Cart> updateCart(final Cart cart, final CheckoutAddressPageContent content) {
        final Address shippingAddress = content.getAddressForm().getShippingAddress().toAddress();
        final Address nullableBillingAddress = content.getAddressForm().isBillingAddressDifferentToBillingAddress()
                ? content.getAddressForm().getBillingAddress().toAddress()
                : null;
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        updateActions.add(SetCountry.of(shippingAddress.getCountry()));
        updateActions.add(SetShippingAddress.of(shippingAddress));
        updateActions.add(SetBillingAddress.of(nullableBillingAddress));
        Optional.ofNullable(shippingAddress.getEmail())
                .ifPresent(email -> updateActions.add(SetCustomerEmail.of(email)));
        return sphere().execute(CartUpdateCommand.of(cart, updateActions));
    }

    private Result renderCheckoutAddressPage(final UserContext userContext, final Cart cart) {
        final CheckoutAddressPageContent content = new CheckoutAddressPageContent(cart, i18nResolver(), configuration(), userContext, projectContext(), productDataConfig, reverseRouter());
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return ok(templateService().renderToHtml("checkout-address", pageData, userContext.locales()));
    }

    private Result badRequest(final UserContext userContext, final Form<CheckoutAddressFormData> filledForm, final CheckoutAddressPageContent content) {
        Logger.info("cart not valid");
        content.getAddressForm().setErrors(new ErrorsBean(filledForm));
        final SunrisePageData pageData = pageData(userContext, content, ctx());
        return badRequest(templateService().renderToHtml("checkout-address", pageData, userContext.locales()));
    }

    private Form<CheckoutAddressFormData> obtainFilledForm(final CheckoutAddressFormData checkoutAddressFormData) {
        final Form<CheckoutAddressFormData> filledForm = formFactory.form(CheckoutAddressFormData.class, CheckoutAddressFormData.Validation.class).bindFromRequest(request());
        additionalValidations(filledForm, checkoutAddressFormData);
        return filledForm;
    }

    private void additionalValidations(final Form<CheckoutAddressFormData> filledForm, final CheckoutAddressFormData checkoutAddressFormData) {
        if (checkoutAddressFormData.isBillingAddressDifferentToBillingAddress() && isBlank(checkoutAddressFormData.getCountryBilling())) {
            filledForm.reject("countryBilling", "form.required");
        }
    }

    private <T> T extractBean(final Http.Request request, final Class<T> clazz) {
        return formFactory.form(clazz, null).bindFromRequest(request).get();
    }
}