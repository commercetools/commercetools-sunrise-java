package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.controllers.SunriseFormController;
import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.address.view.CheckoutAddressPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@IntroducingMultiControllerComponents(CheckoutAddressThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutAddressController<F extends CheckoutAddressFormData> extends SunriseFormController implements WithFormFlow<F, Cart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutAddressExecutor checkoutAddressExecutor;
    private final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory;

    protected SunriseCheckoutAddressController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                               final FormFactory formFactory, final CartFinder cartFinder,
                                               final CheckoutAddressExecutor checkoutAddressExecutor,
                                               final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory) {
        super(hookContext, templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutAddressExecutor = checkoutAddressExecutor;
        this.checkoutAddressPageContentFactory = checkoutAddressPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("checkout", "checkout-address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @SunriseRoute("checkoutAddressesPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::showFormPage));
    }

    @SuppressWarnings("unused")
    @SunriseRoute("checkoutAddressesProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final F formData) {
        return checkoutAddressExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Cart cart, final Form<F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(cart, form);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final F formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<F> form) {
        return checkoutAddressPageContentFactory.create(cart, form);
    }

    @Override
    public Form<F> createForm() {
        return isBillingDifferent()
                ? getFormFactory().form(getFormDataClass(), BillingAddressDifferentToShippingAddressGroup.class)
                : getFormFactory().form(getFormDataClass());
    }

    @Override
    public void preFillFormData(final Cart cart, final F formData) {
        formData.setData(cart);
    }

    protected boolean isBillingDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = getFormFactory().form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }
}