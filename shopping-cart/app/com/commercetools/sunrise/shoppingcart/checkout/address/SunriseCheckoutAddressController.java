package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.address.viewmodels.CheckoutAddressPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutAddressController<F extends CheckoutAddressFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, Cart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutAddressControllerAction checkoutAddressControllerAction;
    private final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory;

    protected SunriseCheckoutAddressController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                               final CartFinder cartFinder,
                                               final CheckoutAddressControllerAction checkoutAddressControllerAction,
                                               final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory) {
        super(templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutAddressControllerAction = checkoutAddressControllerAction;
        this.checkoutAddressPageContentFactory = checkoutAddressPageContentFactory;
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(this::showFormPage);
    }

    @RunRequestStartedHook
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final F formData) {
        return checkoutAddressControllerAction.apply(cart, formData);
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

    protected boolean isBillingDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = getFormFactory().form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }

    @Override
    public void preFillFormData(final Cart cart, final F formData) {
        formData.setData(cart);
    }
}