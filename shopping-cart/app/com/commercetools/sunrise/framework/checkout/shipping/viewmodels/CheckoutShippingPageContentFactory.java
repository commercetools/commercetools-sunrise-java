package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.common.models.carts.CartViewModelFactory;
import com.commercetools.sunrise.framework.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.framework.checkout.shipping.ShippingMethodsWithCart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutShippingPageContentFactory extends FormPageContentFactory<CheckoutShippingPageContent, ShippingMethodsWithCart, CheckoutShippingFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartViewModelFactory cartViewModelFactory;
    private final CheckoutShippingFormSettingsViewModelFactory checkoutShippingFormSettingsViewModelFactory;

    @Inject
    public CheckoutShippingPageContentFactory(final PageTitleResolver pageTitleResolver, final CartViewModelFactory cartViewModelFactory,
                                              final CheckoutShippingFormSettingsViewModelFactory checkoutShippingFormSettingsViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartViewModelFactory = cartViewModelFactory;
        this.checkoutShippingFormSettingsViewModelFactory = checkoutShippingFormSettingsViewModelFactory;
    }

    @Override
    protected CheckoutShippingPageContent getViewModelInstance() {
        return new CheckoutShippingPageContent();
    }

    @Override
    public final CheckoutShippingPageContent create(final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        return super.create(shippingMethodsWithCart, form);
    }

    @Override
    protected final void initialize(final CheckoutShippingPageContent model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        super.initialize(model, shippingMethodsWithCart, form);
        fillCart(model, shippingMethodsWithCart, form);
        fillForm(model, shippingMethodsWithCart, form);
        fillFormSettings(model, shippingMethodsWithCart, form);
    }

    @Override
    protected void fillTitle(final CheckoutShippingPageContent model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutShippingPageContent model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        model.setCart(cartViewModelFactory.create(shippingMethodsWithCart.getCart()));
    }

    protected void fillForm(final CheckoutShippingPageContent model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        model.setShippingForm(form);
    }

    protected void fillFormSettings(final CheckoutShippingPageContent model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        model.setShippingFormSettings(checkoutShippingFormSettingsViewModelFactory.create(shippingMethodsWithCart, form));
    }
}
