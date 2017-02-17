package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.address.view.CheckoutAddressPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutAddressController<F extends CheckoutAddressFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Cart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutAddressExecutor checkoutAddressExecutor;
    private final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory;

    protected SunriseCheckoutAddressController(final ComponentRegistry componentRegistry, final TemplateRenderer templateRenderer,
                                               final FormFactory formFactory, final CartFinder cartFinder,
                                               final CheckoutAddressExecutor checkoutAddressExecutor,
                                               final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory) {
        super(componentRegistry, templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutAddressExecutor = checkoutAddressExecutor;
        this.checkoutAddressPageContentFactory = checkoutAddressPageContentFactory;
    }

    @Inject
    private void registerThemeLinks(final CheckoutAddressThemeLinksControllerComponent themeLinksControllerComponent) {
        register(themeLinksControllerComponent);
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
        return requireNonEmptyCart(this::showFormPage);
    }

    @SuppressWarnings("unused")
    @SunriseRoute("checkoutAddressesProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
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