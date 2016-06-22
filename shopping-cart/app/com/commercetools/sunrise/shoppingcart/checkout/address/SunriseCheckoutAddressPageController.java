package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.common.StepWidgetBean;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetBillingAddress;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;
import scala.concurrent.ExecutionContextExecutor;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public abstract class SunriseCheckoutAddressPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {
    protected static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutAddressPageController.class);

    @Inject
    protected CheckoutAddressPageContentFactory checkoutAddressPageContentFactory;
    @Inject
    private CheckoutReverseRouter checkoutReverseRouter;
    @Inject
    private FormFactory formFactory;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> getOrCreateCart()
                .thenComposeAsync(cart -> showCheckoutAddressPage(cart), HttpExecution.defaultContext()));
    }

    @RequireCSRFCheck
    @SuppressWarnings("unused")
    public CompletionStage<Result> process(final String languageTag) {
        return process(CheckoutAddressFormData.class);
    }

    protected <F extends CheckoutAddressFormDataLike> CompletionStage<Result> process(final Class<F> formClass) {
        return doRequest(() -> {
            final CompletionStage<Cart> loadedCart = getOrCreateCart();
            final ExecutionContextExecutor executor = HttpExecution.defaultContext();
            return loadedCart.thenComposeAsync(cart -> processAddressForm(cart, formClass), executor);

            //TODO waitWithSunrisePageDataHookWhenAllAsyncHooksAreCompleted
            //TODO getOrCreateCart executes hooks
            //TODO is there a solution with config/DI?
            //TODO extension point having data/form
            //TODO log hooks calls in trace
        });
    }

    private CompletionStage<Result> showCheckoutAddressPage(final Cart cart) {
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.create(cart);
        return renderCheckoutAddressPage(cart, pageContent).thenApplyAsync(html -> ok(html));
    }

    protected <F extends CheckoutAddressFormDataLike> CompletionStage<Result> processAddressForm(final Cart cart, final Class<F> formClass) {
        final Form<F> filledForm = bindFormFromRequest(formClass);
        if (filledForm.hasErrors()) {
            return handleFormErrors(filledForm, cart);
        } else {
            final CheckoutAddressFormDataLike formData = filledForm.get();
            final Address shippingAddress = formData.getShippingAddress();
            final Address billingAddress = formData.getBillingAddress();
            return sendAddressesToCommercetoolsPlatform(cart, this, filledForm, shippingAddress, billingAddress);
        }
    }

    protected <F extends CheckoutAddressFormDataLike> CompletionStage<Result> sendAddressesToCommercetoolsPlatform(final Cart cart, final SunriseCheckoutAddressPageController controller, final Form<F> filledForm, final Address shippingAddress, final Address billingAddress) {
        final CompletionStage<Result> resultStage = controller.setAddressToCart(cart, shippingAddress, billingAddress)
                .thenComposeAsync(controller::handleSuccessfulSetAddress, HttpExecution.defaultContext());
        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                controller.handleSetAddressToCartError(throwable, filledForm, cart));
    }

    protected <F extends CheckoutAddressFormDataLike> Form<F> bindFormFromRequest(final Class<F> formClass) {
        final Form<F> form = isBillingDifferent()
                ? formFactory.form(formClass, BillingAddressDifferentToShippingAddressGroup.class)
                : formFactory.form(formClass);
        return form.bindFromRequest();
    }

    private boolean isBillingDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = formFactory.form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }

    protected CompletionStage<Cart> setAddressToCart(final Cart cart,
                                                     final Address shippingAddress,
                                                     @Nullable final Address billingAddress) {
        final List<UpdateAction<Cart>> updateActions = new ArrayList<>();
        updateActions.add(SetCountry.of(shippingAddress.getCountry()));
        updateActions.add(SetShippingAddress.of(shippingAddress));
        updateActions.add(SetBillingAddress.of(billingAddress));
        Optional.ofNullable(shippingAddress.getEmail())
                .ifPresent(email -> updateActions.add(SetCustomerEmail.of(email)));
        return sphere().execute(CartUpdateCommand.of(cart, updateActions));
    }

    protected CompletionStage<Result> handleSuccessfulSetAddress(final Cart cart) {
        final Call call = checkoutReverseRouter.checkoutShippingPageCall(userContext().locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<? extends CheckoutAddressFormDataLike> shippingAddressForm, final Cart cart) {
        ErrorsBean errors = null;
        if (shippingAddressForm.hasErrors()) {
            errors = new ErrorsBean(shippingAddressForm);
        }
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.createWithAddressError(shippingAddressForm, errors);
        return renderCheckoutAddressPage(cart, pageContent).thenApplyAsync(html -> badRequest(html), HttpExecution.defaultContext());
    }

    protected CompletionStage<Result> handleSetAddressToCartError(final Throwable throwable,
                                                                  final Form<? extends CheckoutAddressFormDataLike> shippingAddressForm,
                                                                  final Cart cart) {
        if (throwable.getCause() instanceof ErrorResponseException) {
            return handleErrorResponseException(throwable, shippingAddressForm, cart);
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    private CompletionStage<Result> handleErrorResponseException(final Throwable throwable, final Form<? extends CheckoutAddressFormDataLike> shippingAddressForm, final Cart cart) {
        final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
        logger.error("The request to set address to cart raised an exception", errorResponseException);
        final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
        final CheckoutAddressPageContent pageContent = checkoutAddressPageContentFactory.createWithAddressError(shippingAddressForm, errors);
        return renderCheckoutAddressPage(cart, pageContent).thenApplyAsync(html -> badRequest(html), HttpExecution.defaultContext());
    }

    protected CompletionStage<Html> renderCheckoutAddressPage(final Cart cart, final CheckoutAddressPageContent pageContent) {
        fill(cart, pageContent);
        return renderPage(pageContent, getTemplateName());
    }

    protected void fill(final Cart cart, final CheckoutAddressPageContent pageContent) {
        pageContent.setStepWidget(StepWidgetBean.ADDRESS);
        pageContent.setCart(cartLikeBeanFactory.create(cart));
    }

    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-address"));
    }
}