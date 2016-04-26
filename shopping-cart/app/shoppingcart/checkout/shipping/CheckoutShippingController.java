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
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;
import shoppingcart.checkout.StepWidgetBean;
import shoppingcart.common.CartController;
import shoppingcart.common.CartOrderBean;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Singleton
public class CheckoutShippingController extends CartController {

    private final Form<CheckoutShippingFormData> shippingForm;
    private final ProductDataConfig productDataConfig;

    @Inject
    public CheckoutShippingController(final ControllerDependency controllerDependency,
                                      final ProductDataConfig productDataConfig, final FormFactory formFactory) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
        this.shippingForm = formFactory.form(CheckoutShippingFormData.class);
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
        final Form<CheckoutShippingFormData> shippingBoundForm = shippingForm.bindFromRequest();
        return getOrCreateCart(userContext, session())
                .thenComposeAsync(cart -> {
                    if (shippingBoundForm.hasErrors()) {
                        return getShippingMethods(session())
                                .thenApplyAsync(shippingMethods -> handleFormErrors(shippingBoundForm, shippingMethods, cart, userContext));
                    } else {
                        final String shippingMethodId = shippingBoundForm.get().getShippingMethodId();
                        return setShippingToCart(cart, shippingMethodId)
                                .thenApplyAsync(updatedCart -> handleSuccessfulSetShipping(userContext), HttpExecution.defaultContext());
                    }
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> setShippingToCart(final Cart cart, final String shippingMethodId) {
        final Reference<ShippingMethod> shippingMethodRef = Reference.of(ShippingMethod.referenceTypeId(), shippingMethodId);
        final SetShippingMethod setShippingMethod = SetShippingMethod.of(shippingMethodRef);
        return sphere().execute(CartUpdateCommand.of(cart, setShippingMethod));
    }

    protected Result handleSuccessfulSetShipping(final UserContext userContext) {
        return redirect(reverseRouter().showCheckoutPaymentForm(userContext.locale().toLanguageTag()));
    }

    protected Result handleFormErrors(final Form<CheckoutShippingFormData> shippingBoundForm,
                                      final List<ShippingMethod> shippingMethods,
                                      final Cart cart, final UserContext userContext) {
        final ErrorsBean errors = new ErrorsBean(shippingBoundForm);
        final CheckoutShippingPageContent pageContent = createPageContentWithShippingError(shippingBoundForm, errors, shippingMethods);
        return badRequest(renderCheckoutShippingPage(cart, pageContent, userContext));
    }

    protected CheckoutShippingPageContent createPageContent(final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = new CheckoutShippingPageContent();
        pageContent.setShippingForm(new CheckoutShippingFormBean(shippingMethods, cart.getShippingInfo()));
        return pageContent;
    }

    protected CheckoutShippingPageContent createPageContentWithShippingError(final Form<CheckoutShippingFormData> shippingBoundForm,
                                                                             final ErrorsBean errors, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent pageContent = new CheckoutShippingPageContent();
        final CheckoutShippingFormBean formBean = new CheckoutShippingFormBean(shippingMethods, shippingBoundForm);
        formBean.setErrors(errors);
        pageContent.setShippingForm(formBean);
        return pageContent;
    }

    protected Html renderCheckoutShippingPage(final Cart cart, final CheckoutShippingPageContent pageContent, final UserContext userContext) {
        final StepWidgetBean stepWidget = new StepWidgetBean();
        stepWidget.setShippingStepActive(true);
        pageContent.setStepWidget(stepWidget);
        pageContent.setCart(new CartOrderBean(cart, userContext, productDataConfig, reverseRouter()));
        pageContent.setAdditionalTitle(i18nResolver().getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
        final SunrisePageData pageData = pageData(userContext, pageContent, ctx(), session());
        return templateService().renderToHtml("checkout-shipping", pageData, userContext.locales());
    }
}