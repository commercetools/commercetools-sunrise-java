package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentForm2Flow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.models.BlankPageContent;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutAddressController extends SunriseContentFormController implements WithContentForm2Flow<Void, Cart, CheckoutAddressFormData> {

    private final CheckoutAddressFormData formData;
    private final CheckoutAddressControllerAction controllerAction;

    protected SunriseCheckoutAddressController(final ContentRenderer contentRenderer,
                                               final FormFactory formFactory, final CheckoutAddressFormData formData,
                                               final CheckoutAddressControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @Override
    public final Class<? extends CheckoutAddressFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PAGE)
    public CompletionStage<Result> show() {
        return showFormPage(null, formData); // TODO it required non-empty cart
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null); // TODO it required non-empty cart
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final CheckoutAddressFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutAddressFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends CheckoutAddressFormData> form) {
        return new BlankPageContent();
    }

    @Override
    public Form<? extends CheckoutAddressFormData> createForm() {
        if (isBillingAddressDifferent()) {
            return getFormFactory().form(getFormDataClass(), BillingAddressDifferentToShippingAddressGroup.class);
        }
        return WithContentForm2Flow.super.createForm();
    }

    protected boolean isBillingAddressDifferent() {
        final String flagFieldName = "billingAddressDifferentToBillingAddress";
        final String fieldValue = getFormFactory().form().bindFromRequest().get(flagFieldName);
        return "true".equals(fieldValue);
    }

    @Override
    public void preFillFormData(final Void input, final CheckoutAddressFormData formData) {
//        formData.applyCart(cart);
    }
}