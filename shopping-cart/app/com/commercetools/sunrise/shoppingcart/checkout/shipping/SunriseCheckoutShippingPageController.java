package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.SimpleFormBindingControllerTrait;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.forms.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Call;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

public abstract class SunriseCheckoutShippingPageController extends SunriseFrameworkCartController
        implements WithOverwriteableTemplateName, SimpleFormBindingControllerTrait<CheckoutShippingFormDataLike> {
    private static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutShippingPageController.class);

    @Inject
    private Injector injector;
    @Inject
    private CheckoutShippingPageContentFactory pageContentFactory;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            final CompletionStage<List<ShippingMethod>> shippingMethodsStage = getShippingMethods();
            final CompletionStage<Cart> cartStage = getOrCreateCart();
            return cartStage.thenComposeAsync(cart -> shippingMethodsStage
                    .thenCompose(shippingMethods -> show(cart, shippingMethods)), defaultContext());
        });
    }

    protected CompletionStage<Result> show(final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = pageContentFactory.create(cart, shippingMethods);
        return asyncOk(renderPage(pageContent, getTemplateName()));
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return formProcessingAction(this);
    }

    @Override
    public Class<? extends CheckoutShippingFormDataLike> getFormDataClass() {
        return CheckoutShippingFormData.class;
    }

    @Override
    public CompletionStage<Result> handleValidForm(final Form<? extends CheckoutShippingFormDataLike> form) {
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
        final String shippingMethodId = form.get().getShippingMethodId();
        final CompletionStage<Result> resultStage = setShippingToCart(cart, shippingMethodId)
                .thenComposeAsync(updatedCart -> handleSuccessfulSetShipping(), defaultContext());
                    return recoverWithAsync(resultStage, defaultContext(), throwable ->
                            handleSetShippingToCartError(throwable, form, cart));
                }, defaultContext());
    }

    protected CompletionStage<Cart> setShippingToCart(final Cart cart, final String shippingMethodId) {
        final Reference<ShippingMethod> shippingMethodRef = ShippingMethod.referenceOfId(shippingMethodId);
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        return sphere().execute(CartUpdateCommand.of(cart, setShippingMethod));
    }

    protected CompletionStage<Result> handleSuccessfulSetShipping() {
        final Call call = injector.getInstance(CheckoutReverseRouter.class)
                .checkoutPaymentPageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Form<? extends CheckoutShippingFormDataLike> shippingForm) {
        return getOrCreateCart()
                .thenComposeAsync(cart -> {
                    final ErrorsBean errors = new ErrorsBean(shippingForm);
                    return renderErrorForm(shippingForm, cart, errors);
                }, defaultContext());
    }

    protected CompletionStage<Result> handleSetShippingToCartError(final Throwable throwable,
                                                                   final Form<? extends CheckoutShippingFormDataLike> shippingForm,
                                                                   final Cart cart) {
        logger.error("The request to set shipping to cart raised an exception", throwable);
        final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
        return renderErrorForm(shippingForm, cart, errors);
    }

    protected CompletionStage<Result> renderErrorForm(final Form<? extends CheckoutShippingFormDataLike> shippingForm, final Cart cart, final ErrorsBean errors) {
        return getShippingMethods()
                .thenComposeAsync(shippingMethods -> {
                    final CheckoutShippingPageContent pageContent = pageContentFactory.create(cart, shippingMethods, errors, shippingForm);
                    return asyncBadRequest(renderPage(pageContent, getTemplateName()));
                }, defaultContext());
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