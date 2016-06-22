package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.common.StepWidgetBean;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.SphereException;
import io.sphere.sdk.shippingmethods.ShippingMethod;
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
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.FormUtils.extractFormField;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
public abstract class SunriseCheckoutShippingPageController extends SunriseFrameworkCartController implements WithOverwriteableTemplateName {

    @Inject
    private FormFactory formFactory;
    @Inject
    private CheckoutReverseRouter checkoutReverseRouter;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            final CompletionStage<List<ShippingMethod>> shippingMethodsStage = getShippingMethods(session());
            final CompletionStage<Cart> cartStage = getOrCreateCart();
            return cartStage
                    .thenComposeAsync(cart -> shippingMethodsStage
                            .thenComposeAsync(shippingMethods -> {
                                final CheckoutShippingPageContent pageContent = createPageContent(cart, shippingMethods);
                                return asyncOk(renderCheckoutShippingPage(cart, pageContent, userContext()));
                            }, HttpExecution.defaultContext()), HttpExecution.defaultContext());
        });
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            final Form<CheckoutShippingFormData> shippingForm = formFactory.form(CheckoutShippingFormData.class).bindFromRequest();
            return getOrCreateCart(userContext(), session())
                    .thenComposeAsync(cart -> {
                        if (shippingForm.hasErrors()) {
                            return handleFormErrors(shippingForm, cart, userContext());
                        } else {
                            final String shippingMethodId = shippingForm.get().getShippingMethodId();
                            final CompletionStage<Result> resultStage = setShippingToCart(cart, shippingMethodId)
                                    .thenComposeAsync(updatedCart -> handleSuccessfulSetShipping(userContext()), HttpExecution.defaultContext());
                            return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                    handleSetShippingToCartError(throwable, shippingForm, cart, userContext()));
                        }
                    }, HttpExecution.defaultContext());
        });
    }

    protected CompletionStage<Cart> setShippingToCart(final Cart cart, final String shippingMethodId) {
        final Reference<ShippingMethod> shippingMethodRef = ShippingMethod.referenceOfId(shippingMethodId);
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        return sphere().execute(CartUpdateCommand.of(cart, setShippingMethod));
    }

    protected CompletionStage<Result> handleSuccessfulSetShipping(final UserContext userContext) {
        final Call call = checkoutReverseRouter.checkoutPaymentPageCall(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutShippingFormData> shippingForm,
                                                       final Cart cart, final UserContext userContext) {
        return getShippingMethods(session())
                .thenComposeAsync(shippingMethods -> {
                    final ErrorsBean errors = new ErrorsBean(shippingForm);
                    final CheckoutShippingPageContent pageContent = createPageContentWithShippingError(shippingForm, errors, shippingMethods);
                    return asyncBadRequest(renderCheckoutShippingPage(cart, pageContent, userContext));
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Result> handleSetShippingToCartError(final Throwable throwable,
                                                                   final Form<CheckoutShippingFormData> shippingForm,
                                                                   final Cart cart, final UserContext userContext) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            Logger.error("The request to set shipping to cart raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            return getShippingMethods(session())
                    .thenComposeAsync(shippingMethods -> {
                        final CheckoutShippingPageContent pageContent = createPageContentWithShippingError(shippingForm, errors, shippingMethods);
                        return asyncBadRequest(renderCheckoutShippingPage(cart, pageContent, userContext));
                    }, HttpExecution.defaultContext());
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CheckoutShippingPageContent createPageContent(final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = new CheckoutShippingPageContent();
        final String selectedShippingMethodId = Optional.ofNullable(cart.getShippingInfo())
                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId))
                .orElse(null);
        pageContent.setShippingForm(new CheckoutShippingFormBean(shippingMethods, selectedShippingMethodId));
        return pageContent;
    }

    protected CheckoutShippingPageContent createPageContentWithShippingError(final Form<CheckoutShippingFormData> shippingForm,
                                                                             final ErrorsBean errors, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = new CheckoutShippingPageContent();
        final String selectedShippingMethodId = extractFormField(shippingForm, "shippingMethodId");
        final CheckoutShippingFormBean formBean = new CheckoutShippingFormBean(shippingMethods, selectedShippingMethodId);
        formBean.setErrors(errors);
        pageContent.setShippingForm(formBean);
        return pageContent;
    }

    protected CompletionStage<Html> renderCheckoutShippingPage(final Cart cart, final CheckoutShippingPageContent pageContent, final UserContext userContext) {
        pageContent.setStepWidget(StepWidgetBean.SHIPPING);
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        setI18nTitle(pageContent, "checkout:shippingPage.title");
        return renderPage(pageContent, getTemplateName());
    }

    @Override
    public String getTemplateName() {
        return "checkout-shipping";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-shipping"));
    }
}