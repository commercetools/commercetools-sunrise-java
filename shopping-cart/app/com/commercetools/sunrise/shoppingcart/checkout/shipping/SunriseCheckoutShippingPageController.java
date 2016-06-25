package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.controllers.WithOverridablePageContent;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.errors.ErrorsBean;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.google.inject.Injector;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetShippingMethod;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.models.SphereException;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.common.utils.FormUtils.extractFormField;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@Singleton
public abstract class SunriseCheckoutShippingPageController extends SunriseFrameworkCartController
        implements WithOverwriteableTemplateName, WithOverridablePageContent<CheckoutShippingPageContent> {
    private static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutShippingPageController.class);

    @Inject
    private FormFactory formFactory;
    @Inject
    private Injector injector;
    @Inject
    private CheckoutShippingFormBeanFactory checkoutShippingFormBeanFactory;

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> {
            final CompletionStage<List<ShippingMethod>> shippingMethodsStage = getShippingMethods();
            final CompletionStage<Cart> cartStage = getOrCreateCart();
            return cartStage
                    .thenComposeAsync(cart -> shippingMethodsStage
                            .thenCompose(shippingMethods -> show(cart, shippingMethods)), defaultContext());
        });
    }

    protected CompletionStage<Result> show(final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = createPageContent();
        final CheckoutShippingFormBean shippingFormBean = checkoutShippingFormBeanFactory.create(cart, shippingMethods);
        pageContent.setShippingForm(shippingFormBean);
        return asyncOk(renderCheckoutShippingPage(cart, pageContent));
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> {
            final Form<CheckoutShippingFormData> shippingForm = formFactory.form(CheckoutShippingFormData.class).bindFromRequest();
            return getOrCreateCart()
                    .thenComposeAsync(cart -> {
                        if (shippingForm.hasErrors()) {
                            return handleFormErrors(shippingForm, cart);
                        } else {
                            final String shippingMethodId = shippingForm.get().getShippingMethodId();
                            final CompletionStage<Result> resultStage = setShippingToCart(cart, shippingMethodId)
                                    .thenComposeAsync(updatedCart -> handleSuccessfulSetShipping(), defaultContext());
                            return recoverWithAsync(resultStage, defaultContext(), throwable ->
                                    handleSetShippingToCartError(throwable, shippingForm, cart));
                        }
                    }, defaultContext());
        });
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

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutShippingFormData> shippingForm,
                                                       final Cart cart) {
        return getShippingMethods()
                .thenComposeAsync(shippingMethods -> {
                    final ErrorsBean errors = new ErrorsBean(shippingForm);
                    final CheckoutShippingPageContent pageContent = createPageContentWithShippingError(shippingForm, errors, shippingMethods);
                    return asyncBadRequest(renderCheckoutShippingPage(cart, pageContent));
                }, defaultContext());
    }

    protected CompletionStage<Result> handleSetShippingToCartError(final Throwable throwable,
                                                                   final Form<CheckoutShippingFormData> shippingForm,
                                                                   final Cart cart) {
        if (throwable.getCause() instanceof SphereException) {
            final ErrorResponseException errorResponseException = (ErrorResponseException) throwable.getCause();
            logger.error("The request to set shipping to cart raised an exception", errorResponseException);
            final ErrorsBean errors = new ErrorsBean("Something went wrong, please try again"); // TODO get from i18n
            return getShippingMethods()
                    .thenComposeAsync(shippingMethods -> {
                        final CheckoutShippingPageContent pageContent = createPageContentWithShippingError(shippingForm, errors, shippingMethods);
                        return asyncBadRequest(renderCheckoutShippingPage(cart, pageContent));
                    }, defaultContext());
        }
        return exceptionallyCompletedFuture(new IllegalArgumentException(throwable));
    }

    protected CheckoutShippingPageContent createPageContentWithShippingError(final Form<CheckoutShippingFormData> shippingForm,
                                                                             final ErrorsBean errors, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = createPageContent();
        final String selectedShippingMethodId = extractFormField(shippingForm, "shippingMethodId");
        final CheckoutShippingFormBean formBean = checkoutShippingFormBeanFactory.create(shippingMethods, selectedShippingMethodId);
        formBean.setErrors(errors);
        pageContent.setShippingForm(formBean);
        return pageContent;
    }

    protected CompletionStage<Html> renderCheckoutShippingPage(final Cart cart, final CheckoutShippingPageContent pageContent) {
        pageContent.setCart(cartLikeBeanFactory.create(cart));
        setI18nTitle(pageContent, "checkout:shippingPage.title");
        return renderPage(pageContent, getTemplateName());
    }

    @Override
    public CheckoutShippingPageContent createPageContent() {
        return new CheckoutShippingPageContent();
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