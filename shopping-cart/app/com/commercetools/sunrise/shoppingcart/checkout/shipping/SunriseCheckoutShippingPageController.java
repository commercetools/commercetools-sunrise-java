package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.SimpleFormBindingControllerTrait;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.forms.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.hooks.CartUpdateCommandFilterHook;
import com.commercetools.sunrise.hooks.PrimaryCartUpdatedHook;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
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
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

public abstract class SunriseCheckoutShippingPageController extends SunriseFrameworkCartController
        implements WithOverwriteableTemplateName, SimpleFormBindingControllerTrait<CheckoutShippingFormData, Cart, Cart> {
    private static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutShippingPageController.class);

    @Inject
    private CheckoutShippingPageContentFactory pageContentFactory;

    @Override
    public Class<? extends CheckoutShippingFormData> getFormDataClass() {
        return DefaultCheckoutShippingFormData.class;
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> getOrCreateCart().thenComposeAsync(this::showForm, defaultContext()));
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> getOrCreateCart().thenComposeAsync(this::validateForm, defaultContext()));
    }

    @Override
    public CompletionStage<Result> showForm(final Cart cart) {
        return getShippingMethods().thenComposeAsync(shippingMethods -> {
            final CheckoutShippingPageContent pageContent = pageContentFactory.create(cart, shippingMethods);
            return asyncOk(renderPage(pageContent, getTemplateName()));
        });
    }

    @Override
    public CompletionStage<Result> handleInvalidForm(final Form<? extends CheckoutShippingFormData> form, final Cart cart) {
        final ErrorsBean errors = new ErrorsBean(form);
        return renderErrorForm(form, cart, errors);
    }

    @Override
    public CompletionStage<? extends Cart> doAction(final CheckoutShippingFormData formData, final Cart cart) {
        return setShippingToCart(cart, formData.getShippingMethodId());
    }

    @Override
    public CompletionStage<Result> handleFailedAction(final CheckoutShippingFormData formData, final Cart cart, final Throwable throwable) {
        logger.error("The request to set shipping to cart raised an exception", throwable);
        final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
        final Form<? extends CheckoutShippingFormData> filledForm = createFilledForm(formData.getShippingMethodId());
        return renderErrorForm(filledForm, cart, errors);
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CheckoutShippingFormData formData, final Cart oldCart, final Cart updatedCart) {
        final Call call = injector().getInstance(CheckoutReverseRouter.class)
                .checkoutPaymentPageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> renderErrorForm(final Form<? extends CheckoutShippingFormData> shippingForm, final Cart cart, final ErrorsBean errors) {
        return getShippingMethods()
                .thenComposeAsync(shippingMethods -> {
                    final CheckoutShippingPageContent pageContent = pageContentFactory.create(cart, shippingMethods, errors, shippingForm);
                    return asyncBadRequest(renderPage(pageContent, getTemplateName()));
                }, defaultContext());
    }

    protected Form<? extends CheckoutShippingFormData> createFilledForm(final String shippingMethodId) {
        final DefaultCheckoutShippingFormData formData = new DefaultCheckoutShippingFormData();
        formData.setShippingMethodId(shippingMethodId);
        return formFactory().form(DefaultCheckoutShippingFormData.class).fill(formData);
    }


    @Override
    public String getTemplateName() {
        return "checkout-shipping";
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-shipping"));
    }

    private CompletionStage<Cart> setShippingToCart(final Cart cart, final String shippingMethodId) {
        final Reference<ShippingMethod> shippingMethodRef = ShippingMethod.referenceOfId(shippingMethodId);
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        final CartUpdateCommand cmd = CartUpdateCommand.of(cart, setShippingMethod);
        return executeSphereRequestWithHooks(cmd,
                CartUpdateCommandFilterHook.class, CartUpdateCommandFilterHook::filterCartUpdateCommand,
                PrimaryCartUpdatedHook.class, PrimaryCartUpdatedHook::onPrimaryCartUpdated);
    }
}