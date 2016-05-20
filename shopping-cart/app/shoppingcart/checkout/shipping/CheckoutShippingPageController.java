package shoppingcart.checkout.shipping;

import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunrisePageData;
import common.errors.ErrorsBean;
import common.models.ProductDataConfig;
import common.template.i18n.I18nIdentifier;
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
import shoppingcart.common.CartController;
import shoppingcart.common.StepWidgetBean;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static common.utils.FormUtils.extractFormField;
import static io.sphere.sdk.utils.FutureUtils.exceptionallyCompletedFuture;
import static io.sphere.sdk.utils.FutureUtils.recoverWithAsync;
import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
public class CheckoutShippingPageController extends CartController {

    protected final Form<CheckoutShippingFormData> shippingUnboundForm;

    @Inject
    public CheckoutShippingPageController(final ControllerDependency controllerDependency,
                                          final ProductDataConfig productDataConfig, final FormFactory formFactory) {
        super(controllerDependency, productDataConfig);
        this.shippingUnboundForm = formFactory.form(CheckoutShippingFormData.class);
    }

    @AddCSRFToken
    public CompletionStage<Result> show(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        return getOrCreateCart(userContext, session())
                .thenCombineAsync(getShippingMethods(session()), (cart, shippingMethods) -> {
                    final CheckoutShippingPageContent pageContent = createPageContent(cart, shippingMethods);
                    return ok(renderCheckoutShippingPage(cart, pageContent, userContext));
                }, HttpExecution.defaultContext());
    }

    @RequireCSRFCheck
    public CompletionStage<Result> process(final String languageTag) {
        final UserContext userContext = userContext(languageTag);
        final Form<CheckoutShippingFormData> shippingForm = shippingUnboundForm.bindFromRequest();
        return getOrCreateCart(userContext, session())
                .thenComposeAsync(cart -> {
                    if (shippingForm.hasErrors()) {
                        return handleFormErrors(shippingForm, cart, userContext);
                    } else {
                        final String shippingMethodId = shippingForm.get().getShippingMethodId();
                        final CompletionStage<Result> resultStage = setShippingToCart(cart, shippingMethodId)
                                .thenComposeAsync(updatedCart -> handleSuccessfulSetShipping(userContext), HttpExecution.defaultContext());
                        return recoverWithAsync(resultStage, HttpExecution.defaultContext(), throwable ->
                                handleSetShippingToCartError(throwable, shippingForm, cart, userContext));
                    }
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> setShippingToCart(final Cart cart, final String shippingMethodId) {
        final Reference<ShippingMethod> shippingMethodRef = ShippingMethod.referenceOfId(shippingMethodId);
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        return sphere().execute(CartUpdateCommand.of(cart, setShippingMethod));
    }

    protected CompletionStage<Result> handleSuccessfulSetShipping(final UserContext userContext) {
        final Call call = reverseRouter().showCheckoutPaymentForm(userContext.locale().toLanguageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleFormErrors(final Form<CheckoutShippingFormData> shippingForm,
                                                       final Cart cart, final UserContext userContext) {
        return getShippingMethods(session())
                .thenApplyAsync(shippingMethods -> {
                    final ErrorsBean errors = new ErrorsBean(shippingForm);
                    final CheckoutShippingPageContent pageContent = createPageContentWithShippingError(shippingForm, errors, shippingMethods);
                    return badRequest(renderCheckoutShippingPage(cart, pageContent, userContext));
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
                    .thenApplyAsync(shippingMethods -> {
                        final CheckoutShippingPageContent pageContent = createPageContentWithShippingError(shippingForm, errors, shippingMethods);
                        return badRequest(renderCheckoutShippingPage(cart, pageContent, userContext));
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

    protected Html renderCheckoutShippingPage(final Cart cart, final CheckoutShippingPageContent pageContent, final UserContext userContext) {
        pageContent.setStepWidget(StepWidgetBean.SHIPPING);
        pageContent.setCart(createCartLikeBean(cart, userContext));
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateEngine().renderToHtml("checkout-shipping", pageData, userContext.locales());
    }
}