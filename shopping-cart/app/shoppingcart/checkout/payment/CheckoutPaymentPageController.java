package shoppingcart.checkout.payment;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.payments.PaymentDraft;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import io.sphere.sdk.payments.commands.PaymentCreateCommand;
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

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

@Singleton
public class CheckoutPaymentPageController extends CartController {

    private final Form<CheckoutPaymentFormData> paymentUnboundForm;
    private final ProductDataConfig productDataConfig;
    private final List<PaymentMethodInfo> paymentMethodsInfo;

    @Inject
    public CheckoutPaymentPageController(final ControllerDependency controllerDependency,
                                         final ProductDataConfig productDataConfig, final FormFactory formFactory) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
        this.paymentUnboundForm = formFactory.form(CheckoutPaymentFormData.class);
        this.paymentMethodsInfo = singletonList(PaymentMethodInfoBuilder.of()
                .name(LocalizedString.of(ENGLISH, "Prepaid", GERMAN, "Prepaid")) // TODO pull out
                .method("prepaid")
                .build());
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .thenApplyAsync(cart -> {
                    final CheckoutPaymentPageContent pageContent = createPageContent(cart, paymentMethodsInfo, userContext);
                    return ok(renderCheckoutPaymentPage(cart, pageContent, userContext));
                }, HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<CheckoutPaymentFormData> paymentForm = paymentUnboundForm.bindFromRequest();
        return getOrCreateCart(userContext, session())
                .thenComposeAsync(cart -> {
                    if (paymentForm.hasErrors()) {
                        return completedFuture(handleFormErrors(paymentForm, paymentMethodsInfo, cart, userContext));
                    } else {
                        final String selectedPaymentMethod = paymentForm.get().getPayment();
                        return findPaymentMethodInfoByMethod(selectedPaymentMethod)
                                .map(selectedPaymentInfo -> setPaymentToCart(cart, selectedPaymentInfo)
                                        .thenApplyAsync(updatedCart -> handleSuccessfulSetPayment(userContext), HttpExecution.defaultContext()))
                                .orElseGet(() -> completedFuture(handleInvalidPaymentError(paymentForm, paymentMethodsInfo, cart, userContext)));
                    }
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> setPaymentToCart(final Cart cart, final PaymentMethodInfo selectedPaymentMethod) {
        final PaymentDraft paymentDraft = PaymentDraftBuilder.of(cart.getTotalPrice())
                .paymentMethodInfo(selectedPaymentMethod)
                .customer(Optional.ofNullable(cart.getCustomerId()).map(Customer::referenceOfId).orElse(null))
                .build();
        return sphere().execute(PaymentCreateCommand.of(paymentDraft))
                .thenCompose(payment -> sphere().execute(CartUpdateCommand.of(cart, AddPayment.of(payment))));
    }

    private Optional<PaymentMethodInfo> findPaymentMethodInfoByMethod(final String paymentMethod) {
        return paymentMethodsInfo.stream()
                .filter(info -> info.getMethod() != null && info.getMethod().equals(paymentMethod))
                .findFirst();
    }

    protected Result handleSuccessfulSetPayment(final UserContext userContext) {
        return redirect(reverseRouter().showCheckoutConfirmationForm(userContext.locale().toLanguageTag()));
    }

    protected Result handleFormErrors(final Form<CheckoutPaymentFormData> paymentForm, final List<PaymentMethodInfo> paymentMethods,
                                      final Cart cart, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(paymentForm);
        final CheckoutPaymentPageContent pageContent = createPageContentWithPaymentError(paymentForm, errors, paymentMethods, userContext);
        return badRequest(renderCheckoutPaymentPage(cart, pageContent, userContext));
    }

    protected Result handleInvalidPaymentError(final Form<CheckoutPaymentFormData> paymentForm, final List<PaymentMethodInfo> paymentMethods,
                                               final Cart cart, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean("Invalid payment error"); // TODO use i18n
        final CheckoutPaymentPageContent pageContent = createPageContentWithPaymentError(paymentForm, errors, paymentMethods, userContext);
        return badRequest(renderCheckoutPaymentPage(cart, pageContent, userContext));
    }

    protected CheckoutPaymentPageContent createPageContent(final Cart cart, final List<PaymentMethodInfo> paymentMethods,
                                                           final UserContext userContext) {
        final CheckoutPaymentPageContent pageContent = new CheckoutPaymentPageContent();
        final List<String> selectedPaymentMethod = Optional.ofNullable(cart.getPaymentInfo())
                .map(info -> info.getPayments().stream()
                        .filter(ref -> ref.getObj() != null)
                        .map(ref -> ref.getObj().getPaymentMethodInfo().getMethod())
                        .collect(toList()))
                .orElse(emptyList());
        pageContent.setPaymentForm(new CheckoutPaymentFormBean(paymentMethods, selectedPaymentMethod, userContext));
        return pageContent;
    }

    protected CheckoutPaymentPageContent createPageContentWithPaymentError(final Form<CheckoutPaymentFormData> paymentForm,
                                                                           final ErrorsBean errors, final List<PaymentMethodInfo> paymentMethods,
                                                                           final UserContext userContext) {
        final CheckoutPaymentPageContent pageContent = new CheckoutPaymentPageContent();
        final List<String> selectedPaymentMethodKeys = Optional.ofNullable(paymentForm.field("payment").value())
                .map(Collections::singletonList)
                .orElse(emptyList());
        final CheckoutPaymentFormBean formBean = new CheckoutPaymentFormBean(paymentMethods, selectedPaymentMethodKeys, userContext);
        formBean.setErrors(errors);
        pageContent.setPaymentForm(formBean);
        return pageContent;
    }

    protected Html renderCheckoutPaymentPage(final Cart cart, final CheckoutPaymentPageContent pageContent, final UserContext userContext) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        pageContent.setStepWidget(stepWidget);
        pageContent.setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter()));
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:paymentPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("checkout-payment", pageData, userContext.locales());
    }
}
