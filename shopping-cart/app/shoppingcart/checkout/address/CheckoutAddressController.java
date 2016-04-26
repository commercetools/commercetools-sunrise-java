package shoppingcart.checkout.address;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.checkout.StepWidgetBean;
import shoppingcart.common.CartController;
import shoppingcart.common.CartOrderBean;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
public class CheckoutAddressController extends CartController {

    private final Form<CheckoutShippingAddressFormData> shippingAddressForm;
    private final Form<CheckoutBillingAddressFormData> billingAddressForm;
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutAddressController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig,
                                     final FormFactory formFactory) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
        this.shippingAddressForm = formFactory.form(CheckoutShippingAddressFormData.class);
        this.billingAddressForm = formFactory.form(CheckoutBillingAddressFormData.class);
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .thenApplyAsync(cart -> {
                    final CheckoutAddressPageContent pageContent = createPageContent(cart, userContext);
                    return ok(renderCheckoutAddressPage(cart, pageContent, userContext));
                }, HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<CheckoutShippingAddressFormData> shippingAddressBoundForm = shippingAddressForm.bindFromRequest();
        final Form<CheckoutBillingAddressFormData> billingAddressBoundForm = billingAddressForm.bindFromRequest();
        return getOrCreateCart(userContext, session())
                .thenComposeAsync(cart -> {
                    if (formHasErrors(shippingAddressBoundForm, billingAddressBoundForm)) {
                        return completedFuture(handleFormErrors(shippingAddressBoundForm, billingAddressBoundForm, cart, userContext));
                    } else {
                        final Address shippingAddress = shippingAddressBoundForm.get().toAddress();
                        final boolean differentBilling = shippingAddressBoundForm.get().isBillingAddressDifferentToBillingAddress();
                        final Address billingAddress = differentBilling ? billingAddressBoundForm.get().toAddress() : null;
                        return setAddressToCart(cart, shippingAddress, billingAddress)
                                .thenApplyAsync(updatedCart -> handleSuccessfulSetAddress(userContext), HttpExecution.defaultContext());
                    }
                }, HttpExecution.defaultContext());
    }

    protected boolean formHasErrors(final Form<CheckoutShippingAddressFormData> shippingAddressBoundForm,
                                  final Form<CheckoutBillingAddressFormData> billingAddressBoundForm) {
        if (shippingAddressBoundForm.hasErrors()) {
            return true;
        } else {
            final boolean differentBilling = shippingAddressBoundForm.get().isBillingAddressDifferentToBillingAddress();
            return differentBilling && billingAddressBoundForm.hasErrors();
        }
    }

    protected CompletionStage<Cart> setAddressToCart(final Cart cart, final Address shippingAddress,
                                                     final @Nullable Address billingAddress) {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        updateActions.add(SetCountry.of(shippingAddress.getCountry()));
        updateActions.add(SetShippingAddress.of(shippingAddress));
        updateActions.add(SetBillingAddress.of(billingAddress));
        Optional.ofNullable(shippingAddress.getEmail())
                .ifPresent(email -> updateActions.add(SetCustomerEmail.of(email)));
        return sphere().execute(CartUpdateCommand.of(cart, updateActions));
    }

    protected Result handleSuccessfulSetAddress(final UserContext userContext) {
        return redirect(reverseRouter().showCheckoutShippingForm(userContext.locale().toLanguageTag()));
    }

    protected Result handleFormErrors(final Form<CheckoutShippingAddressFormData> shippingAddressBoundForm,
                                      final Form<CheckoutBillingAddressFormData> billingAddressBoundForm,
                                      final Cart cart, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(asList(shippingAddressBoundForm, billingAddressBoundForm));
        final CheckoutAddressPageContent pageContent = createPageContentWithAddressError(shippingAddressBoundForm, billingAddressBoundForm, errors, userContext);
        return badRequest(renderCheckoutAddressPage(cart, pageContent, userContext));
    }

    protected CheckoutAddressPageContent createPageContent(final Cart cart, final UserContext userContext) {
        final CheckoutAddressPageContent pageContent = new CheckoutAddressPageContent();
        final Address shippingAddress = cart.getShippingAddress();
        final Address billingAddress = cart.getBillingAddress();
        pageContent.setAddressForm(new CheckoutAddressFormBean(shippingAddress, billingAddress, userContext, projectContext(), i18nResolver(), configuration()));
        return pageContent;
    }

    protected CheckoutAddressPageContent createPageContentWithAddressError(final Form<CheckoutShippingAddressFormData> shippingAddressBoundForm,
                                                                           final Form<CheckoutBillingAddressFormData> billingAddressBoundForm,
                                                                           final ErrorsBean errors, final UserContext userContext) {
        final CheckoutAddressPageContent pageContent = new CheckoutAddressPageContent();
        final CheckoutAddressFormBean formBean = new CheckoutAddressFormBean(shippingAddressBoundForm, billingAddressBoundForm, userContext, projectContext(), i18nResolver(), configuration());
        formBean.setErrors(errors);
        pageContent.setAddressForm(formBean);
        return pageContent;
    }

    protected Html renderCheckoutAddressPage(final Cart cart, final CheckoutAddressPageContent pageContent, final UserContext userContext) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setAddressStepActive(true);
        pageContent.setStepWidget(stepWidget);
        pageContent.setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter()));
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("checkout-address", pageData, userContext.locales());
    }
}