package shoppingcart.checkout.confirmation;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.concurrent.CompletionStage;

import static common.utils.FormUtils.extractBooleanFormField;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static shoppingcart.CartSessionUtils.removeCartSessionData;
import static shoppingcart.OrderSessionUtils.overwriteLastOrderIdSessionData;

@Singleton
public class CheckoutConfirmationPageController extends CartController {

    protected final Form<CheckoutConfirmationFormData> confirmationUnboundForm;

    @Inject
    public CheckoutConfirmationPageController(final ControllerDependency controllerDependency,
                                              final ProductDataConfig productDataConfig, final FormFactory formFactory) {
        super(controllerDependency, productDataConfig);
        this.confirmationUnboundForm = formFactory.form(CheckoutConfirmationFormData.class);
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .thenApplyAsync(cart -> {
                    final CheckoutConfirmationPageContent pageContent = createPageContent();
                    return ok(renderCheckoutConfirmationPage(cart, pageContent, userContext));
                }, HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<CheckoutConfirmationFormData> confirmationForm = confirmationUnboundForm.bindFromRequest(request());
        return getOrCreateCart(userContext, session())
                .thenComposeAsync(cart -> {
                    if (confirmationForm.hasErrors()) {
                        return handleFormErrors(confirmationForm, cart, userContext);
                    } else {
                        final CompletionStage<Result> resultStage = createOrder(cart)
                                .thenComposeAsync(order -> handleSuccessfulCreateOrder(userContext), HttpExecution.defaultContext());
                        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                handleCreateOrderError(throwable, confirmationForm, cart, userContext));
                    }
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Order> createOrder(final Cart cart) {
        final String orderNumber = generateOrderNumber();
        final OrderFromCartDraft orderDraft = OrderFromCartDraft.of(cart, orderNumber, PaymentState.PAID);
        return sphere().execute(OrderFromCartCreateCommand.of(orderDraft))
                .thenApplyAsync(order -> {
                    overwriteLastOrderIdSessionData(order, session());
                    removeCartSessionData(session());
                    return order;
                }, HttpExecution.defaultContext());
    }

    protected String generateOrderNumber() {
        return RandomStringUtils.randomNumeric(8);
    }

    protected CompletionStage<Result> handleSuccessfulCreateOrder(final UserContext userContext) {
        final Call call = reverseRouter().showCheckoutThankYou(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutConfirmationFormData> confirmationForm, final Cart cart,
                                      final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(confirmationForm);
        final CheckoutConfirmationPageContent pageContent = createPageContentWithConfirmationError(confirmationForm, errors);
        return completedFuture(badRequest(renderCheckoutConfirmationPage(cart, pageContent, userContext)));
    }

    protected CompletionStage<Result> handleCreateOrderError(final Throwable throwable,
                                                             final Form<CheckoutConfirmationFormData> confirmationForm,
                                                             final Cart cart, final UserContext userContext) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to create the order raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CheckoutConfirmationPageContent pageContent = createPageContentWithConfirmationError(confirmationForm, errors);
            return completedFuture(badRequest(renderCheckoutConfirmationPage(cart, pageContent, userContext)));
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CheckoutConfirmationPageContent createPageContent() {
        final CheckoutConfirmationPageContent pageContent = new CheckoutConfirmationPageContent();
        pageContent.setCheckoutForm(new CheckoutConfirmationFormBean());
        return pageContent;
    }

    protected CheckoutConfirmationPageContent createPageContentWithConfirmationError(final Form<CheckoutConfirmationFormData> confirmationForm,
                                                                                     final ErrorsBean errors) {
        final CheckoutConfirmationPageContent pageContent = new CheckoutConfirmationPageContent();
        final boolean agreeToTerms = extractBooleanFormField(confirmationForm, "agreeTerms");
        final CheckoutConfirmationFormBean formBean = new CheckoutConfirmationFormBean(agreeToTerms);
        formBean.setErrors(errors);
        pageContent.setCheckoutForm(formBean);
        return pageContent;
    }

    protected StepWidgetBean createStepWidgetBean() {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setConfirmationStepActive(true);
        return stepWidget;
    }

    protected Html renderCheckoutConfirmationPage(final Cart cart, final CheckoutConfirmationPageContent pageContent, final UserContext userContext) {
        pageContent.setStepWidget(createStepWidgetBean());
        pageContent.setCart(createCartLikeBean(cart, userContext));
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:confirmationPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("checkout-confirmation", pageData, userContext.locales());
    }
}
