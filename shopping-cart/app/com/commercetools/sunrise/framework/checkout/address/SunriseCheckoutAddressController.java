package com.commercetools.sunrise.framework.checkout.address;

import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.WithRequiredCart;
import com.commercetools.sunrise.framework.checkout.address.viewmodels.CheckoutAddressPageContentFactory;
import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutAddressController extends SunriseContentFormController
        implements WithContentFormFlow<Cart, Cart, CheckoutAddressFormData>, WithRequiredCart {

    private final CheckoutAddressFormData formData;
    private final CartFinder cartFinder;
    private final CheckoutAddressControllerAction controllerAction;
    private final CheckoutAddressPageContentFactory pageContentFactory;

    protected SunriseCheckoutAddressController(final ContentRenderer contentRenderer,
                                               final FormFactory formFactory, final CheckoutAddressFormData formData,
                                               final CartFinder cartFinder,
                                               final CheckoutAddressControllerAction controllerAction,
                                               final CheckoutAddressPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends CheckoutAddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(cart -> showFormPage(cart, formData));
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(this::processForm);
    }

    @Override
    public CompletionStage<Cart> executeAction(final Cart cart, final CheckoutAddressFormData formData) {
        return controllerAction.apply(cart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutAddressFormData formData);

    @Override
    public PageContent createPageContent(final Cart cart, final Form<? extends CheckoutAddressFormData> form) {
        return pageContentFactory.create(cart, form);
    }

    @Override
    public Form<? extends CheckoutAddressFormData> createForm() {
        if (isBillingAddressDifferent()) {
            return getFormFactory().form(getFormDataClass(), BillingAddressDifferentToShippingAddressGroup.class);
        }
        return WithContentFormFlow.super.createForm();
    }

    protected boolean isBillingAddressDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = getFormFactory().form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }

    @Override
    public void preFillFormData(final Cart cart, final CheckoutAddressFormData formData) {
        formData.applyCart(cart);
    }
}