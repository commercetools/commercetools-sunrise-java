package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.common.StepWidgetBean;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
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

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.FormUtils.extractBooleanFormField;
import static com.commercetools.sunrise.shoppingcart.CartSessionUtils.removeCartSessionData;
import static com.commercetools.sunrise.shoppingcart.OrderSessionUtils.overwriteLastOrderIdSessionData;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public abstract class SunriseCheckoutConfirmationPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {

    @Inject
    private FormFactory formFactory;
    @Inject
    private CheckoutReverseRouter checkoutReverseRouter;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            return getOrCreateCart()
                    .thenComposeAsync(cart -> {
                        final CheckoutConfirmationPageContent pageContent = createPageContent();
                        return asyncOk(renderCheckoutConfirmationPage(cart, pageContent));
                    }, HttpExecution.defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            final Form<CheckoutConfirmationFormData> confirmationForm = formFactory.form(CheckoutConfirmationFormData.class).bindFromRequest(request());
            return getOrCreateCart()
                    .thenComposeAsync(cart -> {
                        if (confirmationForm.hasErrors()) {
                            return handleFormErrors(confirmationForm, cart);
                        } else {
                            final CompletionStage<Result> resultStage = createOrder(cart)
                                    .thenComposeAsync(order -> handleSuccessfulCreateOrder(), HttpExecution.defaultContext());
                            return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                    handleCreateOrderError(throwable, confirmationForm, cart));
                        }
                    }, HttpExecution.defaultContext());
        });
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

    protected CompletionStage<Result> handleSuccessfulCreateOrder() {
        final Call call = checkoutReverseRouter.checkoutThankYouPageCall(userContext().locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutConfirmationFormData> confirmationForm, final Cart cart) {
        final ErrorsBean errors = new ErrorsBean(confirmationForm);
        final CheckoutConfirmationPageContent pageContent = createPageContentWithConfirmationError(confirmationForm, errors);
        return asyncBadRequest(renderCheckoutConfirmationPage(cart, pageContent));
    }

    protected CompletionStage<Result> handleCreateOrderError(final Throwable throwable,
                                                             final Form<CheckoutConfirmationFormData> confirmationForm,
                                                             final Cart cart) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to create the order raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            final CheckoutConfirmationPageContent pageContent = createPageContentWithConfirmationError(confirmationForm, errors);
            return asyncBadRequest(renderCheckoutConfirmationPage(cart, pageContent));
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

    protected CompletionStage<Html> renderCheckoutConfirmationPage(final Cart cart, final CheckoutConfirmationPageContent pageContent) {
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        setI18nTitle(pageContent, "checkout:confirmationPage.title");
        return renderPage(pageContent, getTemplateName());
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-confirmation"));
    }

    @Override
    public String getTemplateName() {
        return "checkout-confirmation";
    }
}
