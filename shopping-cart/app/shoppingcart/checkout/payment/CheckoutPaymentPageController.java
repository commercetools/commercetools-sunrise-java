package shoppingcart.checkout.payment;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import common.utils.FutureUtils;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.PaymentInfo;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.carts.commands.updateactions.RemovePayment;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.*;
import io.sphere.sdk.payments.commands.PaymentCreateCommand;
import io.sphere.sdk.payments.commands.PaymentDeleteCommand;
import io.sphere.sdk.payments.queries.PaymentByIdGet;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.common.CartController;
import shoppingcart.common.StepWidgetBean;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static common.utils.FormUtils.extractFormField;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

@Singleton
public class CheckoutPaymentPageController extends CartController {

    protected final Form<CheckoutPaymentFormData> paymentUnboundForm;
    protected final List<PaymentMethodInfo> paymentMethodsInfo;

    @Inject
    public CheckoutPaymentPageController(final ControllerDependency controllerDependency,
                                         final ProductDataConfig productDataConfig, final FormFactory formFactory) {
        super(controllerDependency, productDataConfig);
        this.paymentUnboundForm = formFactory.form(CheckoutPaymentFormData.class);
        this.paymentMethodsInfo = singletonList(PaymentMethodInfoBuilder.of()
                .name(LocalizedString.of(Locale.ENGLISH, "Prepaid", Locale.GERMAN, "Vorkasse")) // TODO pull out
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
                        return handleFormErrors(paymentForm, paymentMethodsInfo, cart, userContext);
                    } else {
                        final List<String> selectedMethodNames = singletonList(paymentForm.get().getPayment());
                        final List<PaymentMethodInfo> selectedPaymentMethods = getSelectedPaymentMethodsInfo(selectedMethodNames);
                        if (selectedPaymentMethods.isEmpty()) {
                            return handleInvalidPaymentError(paymentForm, selectedPaymentMethods, cart, userContext);
                        } else {
                            return setPaymentToCart(cart, selectedPaymentMethods)
                                    .thenComposeAsync(updatedCart -> handleSuccessfulSetPayment(userContext), HttpExecution.defaultContext());
                        }
                    }
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> setPaymentToCart(final Cart cart, final List<PaymentMethodInfo> selectedPaymentMethods) {
        return withPaymentsToRemove(cart, selectedPaymentMethods, paymentsToRemove ->
                withPaymentsToAdd(cart, selectedPaymentMethods, paymentsToAdd -> {
                    final Stream<RemovePayment> removePaymentStream = paymentsToRemove.stream().map(RemovePayment::of);
                    final Stream<AddPayment> addPaymentStream = paymentsToAdd.stream().map(AddPayment::of);
                    final List<UpdateAction<Cart>> updateActions = Stream.concat(removePaymentStream, addPaymentStream).collect(toList());
                    return sphere().execute(CartUpdateCommand.of(cart, updateActions));
                })
        );
    }

    protected CompletionStage<Cart> withPaymentsToRemove(final Cart cart, final List<PaymentMethodInfo> selectedPaymentMethods,
                                                         final Function<List<Payment>, CompletionStage<Cart>> setPaymentAction) {
        final List<Reference<Payment>> paymentRefs = Optional.ofNullable(cart.getPaymentInfo())
                .map(PaymentInfo::getPayments)
                .orElseGet(() -> {
                    Logger.error("Payment info is not expanded in cart: the new payment information can be saved but the previous payments will not be removed.");
                    return emptyList();
                });
        final List<CompletionStage<Payment>> paymentStages = paymentRefs.stream()
                .map(paymentRef -> sphere().execute(PaymentByIdGet.of(paymentRef)))
                .collect(toList());
        return FutureUtils.listOfFuturesToFutureOfList(paymentStages)
                .thenComposeAsync(payments -> {
                    payments.removeIf(Objects::isNull);
                    final CompletionStage<Cart> updatedCartStage = setPaymentAction.apply(payments);
                    updatedCartStage.thenAccept(updatedCart ->
                            payments.forEach(payment -> sphere().execute(PaymentDeleteCommand.of(payment))));
                    return updatedCartStage;
                });
    }

    protected CompletionStage<Cart> withPaymentsToAdd(final Cart cart, final List<PaymentMethodInfo> selectedPaymentMethods,
                                                      final Function<List<Payment>, CompletionStage<Cart>> setPaymentAction) {
        final List<CompletionStage<Payment>> paymentStages = selectedPaymentMethods.stream()
                .map(selectedPaymentMethod -> {
                    final PaymentDraft paymentDraft = PaymentDraftBuilder.of(cart.getTotalPrice())
                            .paymentMethodInfo(selectedPaymentMethod)
                            .customer(Optional.ofNullable(cart.getCustomerId()).map(Customer::referenceOfId).orElse(null))
                            .build();
                    return sphere().execute(PaymentCreateCommand.of(paymentDraft));
                })
                .collect(toList());
        return FutureUtils.listOfFuturesToFutureOfList(paymentStages)
                .thenComposeAsync(payments -> {
                    payments.removeIf(Objects::isNull);
                    return setPaymentAction.apply(payments);
                });
    }

    protected List<PaymentMethodInfo> getSelectedPaymentMethodsInfo(final List<String> paymentMethod) {
        return paymentMethodsInfo.stream()
                .filter(info -> info.getMethod() != null && paymentMethod.contains(info.getMethod()))
                .collect(toList());
    }

    protected CompletionStage<Result> handleSuccessfulSetPayment(final UserContext userContext) {
        final Call call = reverseRouter().showCheckoutConfirmationForm(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutPaymentFormData> paymentForm,
                                                       final List<PaymentMethodInfo> paymentMethods,
                                                       final Cart cart, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(paymentForm);
        final CheckoutPaymentPageContent pageContent = createPageContentWithPaymentError(paymentForm, errors, paymentMethods, userContext);
        return completedFuture(badRequest(renderCheckoutPaymentPage(cart, pageContent, userContext)));
    }

    protected CompletionStage<Result> handleInvalidPaymentError(final Form<CheckoutPaymentFormData> paymentForm,
                                                                final List<PaymentMethodInfo> paymentMethods,
                                                                final Cart cart, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean("Invalid payment error"); // TODO use i18n
        final CheckoutPaymentPageContent pageContent = createPageContentWithPaymentError(paymentForm, errors, paymentMethods, userContext);
        return completedFuture(badRequest(renderCheckoutPaymentPage(cart, pageContent, userContext)));
    }

    protected CheckoutPaymentPageContent createPageContent(final Cart cart, final List<PaymentMethodInfo> paymentMethods,
                                                           final UserContext userContext) {
        final CheckoutPaymentPageContent pageContent = new CheckoutPaymentPageContent();
        final List<String> selectedPaymentMethods = Optional.ofNullable(cart.getPaymentInfo())
                .map(info -> info.getPayments().stream()
                        .filter(ref -> ref.getObj() != null)
                        .map(ref -> ref.getObj().getPaymentMethodInfo().getMethod())
                        .collect(toList()))
                .orElse(emptyList());
        pageContent.setPaymentForm(new CheckoutPaymentFormBean(paymentMethods, selectedPaymentMethods, userContext));
        return pageContent;
    }

    protected CheckoutPaymentPageContent createPageContentWithPaymentError(final Form<CheckoutPaymentFormData> paymentForm,
                                                                           final ErrorsBean errors, final List<PaymentMethodInfo> paymentMethods,
                                                                           final UserContext userContext) {
        final CheckoutPaymentPageContent pageContent = new CheckoutPaymentPageContent();
        final List<String> selectedPaymentMethodKeys = Optional.ofNullable(extractFormField(paymentForm, "payment"))
                .map(Collections::singletonList)
                .orElse(emptyList());
        final CheckoutPaymentFormBean formBean = new CheckoutPaymentFormBean(paymentMethods, selectedPaymentMethodKeys, userContext);
        formBean.setErrors(errors);
        pageContent.setPaymentForm(formBean);
        return pageContent;
    }

    protected StepWidgetBean createStepWidgetBean() {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setPaymentStepActive(true);
        return stepWidget;
    }

    protected Html renderCheckoutPaymentPage(final Cart cart, final CheckoutPaymentPageContent pageContent, final UserContext userContext) {
        pageContent.setStepWidget(createStepWidgetBean());
        pageContent.setCart(createCartLikeBean(cart, userContext));
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:paymentPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("checkout-payment", pageData, userContext.locales());
    }
}
