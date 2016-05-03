package shoppingcart.checkout.address;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import common.utils.FormUtils;
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
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.common.StepWidgetBean;
import shoppingcart.common.CartController;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
public class CheckoutAddressPageController extends CartController {

    protected final Form<CheckoutShippingAddressFormData> shippingAddressUnboundForm;
    protected final Form<CheckoutBillingAddressFormData> billingAddressUnboundForm;

    @Inject
    public CheckoutAddressPageController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig,
                                         final FormFactory formFactory) {
        super(controllerDependency, productDataConfig);
        this.shippingAddressUnboundForm = formFactory.form(CheckoutShippingAddressFormData.class);
        this.billingAddressUnboundForm = formFactory.form(CheckoutBillingAddressFormData.class);
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
        final Form<CheckoutShippingAddressFormData> shippingAddressForm = shippingAddressUnboundForm.bindFromRequest();
        final Form<CheckoutBillingAddressFormData> billingAddressForm = billingAddressUnboundForm.bindFromRequest();
        return getOrCreateCart(userContext, session())
                .thenComposeAsync(cart -> {
                    if (formHasErrors(shippingAddressForm, billingAddressForm)) {
                        return handleFormErrors(shippingAddressForm, billingAddressForm, cart, userContext);
                    } else {
                        final Address shippingAddress = shippingAddressForm.get().toAddress();
                        final boolean differentBilling = shippingAddressForm.get().isBillingAddressDifferentToBillingAddress();
                        final Address billingAddress = differentBilling ? billingAddressForm.get().toAddress() : null;
                        return setAddressToCart(cart, shippingAddress, billingAddress)
                                .thenComposeAsync(updatedCart -> handleSuccessfulSetAddress(userContext), HttpExecution.defaultContext());
                    }
                }, HttpExecution.defaultContext());
    }

    protected boolean formHasErrors(final Form<CheckoutShippingAddressFormData> shippingAddressForm,
                                    final Form<CheckoutBillingAddressFormData> billingAddressForm) {
        if (shippingAddressForm.hasErrors()) {
            return true;
        } else {
            final boolean differentBilling = shippingAddressForm.get().isBillingAddressDifferentToBillingAddress();
            return differentBilling && billingAddressForm.hasErrors();
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

    protected CompletionStage<Result> handleSuccessfulSetAddress(final UserContext userContext) {
        final Call call = reverseRouter().showCheckoutShippingForm(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutShippingAddressFormData> shippingAddressForm,
                                      final Form<CheckoutBillingAddressFormData> billingAddressForm,
                                      final Cart cart, final UserContext userContext) {
        final ErrorsBean errors;
        if (shippingAddressForm.hasErrors()) {
            errors = new ErrorsBean(shippingAddressForm);
        } else {
            errors = new ErrorsBean(billingAddressForm);
        }
        final CheckoutAddressPageContent pageContent = createPageContentWithAddressError(shippingAddressForm, billingAddressForm, errors, userContext);
        return completedFuture(badRequest(renderCheckoutAddressPage(cart, pageContent, userContext)));
    }

    protected CheckoutAddressPageContent createPageContent(final Cart cart, final UserContext userContext) {
        final CheckoutAddressPageContent pageContent = new CheckoutAddressPageContent();
        final Address shippingAddress = cart.getShippingAddress();
        final Address billingAddress = cart.getBillingAddress();
        final boolean differentBillingAddress = billingAddress != null;
        pageContent.setAddressForm(new CheckoutAddressFormBean(shippingAddress, billingAddress, differentBillingAddress, userContext, projectContext(), i18nResolver(), configuration()));
        return pageContent;
    }

    protected CheckoutAddressPageContent createPageContentWithAddressError(final Form<CheckoutShippingAddressFormData> shippingAddressForm,
                                                                           final Form<CheckoutBillingAddressFormData> billingAddressForm,
                                                                           final ErrorsBean errors, final UserContext userContext) {
        final CheckoutAddressPageContent pageContent = new CheckoutAddressPageContent();
        final Address shippingAddress = FormUtils.extractAddress(shippingAddressForm, "Shipping");
        final Address billingAddress = FormUtils.extractAddress(billingAddressForm, "Billing");
        final boolean differentBillingAddress = Boolean.valueOf(shippingAddressForm.field("billingAddressDifferentToBillingAddress").value());
        final CheckoutAddressFormBean formBean = new CheckoutAddressFormBean(shippingAddress, billingAddress, differentBillingAddress, userContext, projectContext(), i18nResolver(), configuration());
        formBean.setErrors(errors);
        pageContent.setAddressForm(formBean);
        return pageContent;
    }

    protected StepWidgetBean createStepWidgetBean() {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setAddressStepActive(true);
        return stepWidget;
    }

    protected Html renderCheckoutAddressPage(final Cart cart, final CheckoutAddressPageContent pageContent, final UserContext userContext) {
        pageContent.setStepWidget(createStepWidgetBean());
        pageContent.setCart(createCartLikeBean(cart, userContext));
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("checkout-address", pageData, userContext.locales());
    }
}