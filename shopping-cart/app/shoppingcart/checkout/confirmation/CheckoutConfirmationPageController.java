package shoppingcart.checkout.confirmation;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.CartSessionUtils;
import shoppingcart.OrderSessionUtils;
import shoppingcart.common.StepWidgetBean;
import shoppingcart.common.CartController;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

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
                        return createOrder(cart)
                                .thenComposeAsync(order -> handleSuccessfulCreateOrder(userContext), HttpExecution.defaultContext());
                    }
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Order> createOrder(final Cart cart) {
        final String orderNumber = RandomStringUtils.randomNumeric(8);
        final OrderFromCartDraft orderDraft = OrderFromCartDraft.of(cart, orderNumber, PaymentState.BALANCE_DUE);
        return sphere().execute(OrderFromCartCreateCommand.of(orderDraft))
                .thenApplyAsync(order -> {
                    OrderSessionUtils.overwriteLastOrderId(order, session());
                    CartSessionUtils.removeCart(session());
                    return order;
                }, HttpExecution.defaultContext());
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

    protected CheckoutConfirmationPageContent createPageContent() {
        final CheckoutConfirmationPageContent pageContent = new CheckoutConfirmationPageContent();
        pageContent.setCheckoutForm(new CheckoutConfirmationFormBean());
        return pageContent;
    }

    protected CheckoutConfirmationPageContent createPageContentWithConfirmationError(final Form<CheckoutConfirmationFormData> confirmationForm,
                                                                                     final ErrorsBean errors) {
        final CheckoutConfirmationPageContent pageContent = new CheckoutConfirmationPageContent();
        final boolean agreeToTerms = Boolean.valueOf(confirmationForm.field("agreeTerms").value());
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
