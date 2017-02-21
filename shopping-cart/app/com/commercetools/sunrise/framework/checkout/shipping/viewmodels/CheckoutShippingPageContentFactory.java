package com.commercetools.sunrise.framework.checkout.shipping.viewmodels;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.common.models.carts.CartBeanFactory;
import com.commercetools.sunrise.framework.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.framework.checkout.shipping.ShippingMethodsWithCart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutShippingPageContentFactory extends FormPageContentFactory<CheckoutShippingPageContent, ShippingMethodsWithCart, CheckoutShippingFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;
    private final CheckoutShippingFormSettingsBeanFactory checkoutShippingFormSettingsBeanFactory;

    @Inject
    public CheckoutShippingPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory,
                                              final CheckoutShippingFormSettingsBeanFactory checkoutShippingFormSettingsBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
        this.checkoutShippingFormSettingsBeanFactory = checkoutShippingFormSettingsBeanFactory;
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
        model.setCart(cartBeanFactory.create(shippingMethodsWithCart.getCart()));
    }

    protected void fillForm(final CheckoutShippingPageContent model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        model.setShippingForm(form);
    }

    protected void fillFormSettings(final CheckoutShippingPageContent model, final ShippingMethodsWithCart shippingMethodsWithCart, final Form<? extends CheckoutShippingFormData> form) {
        model.setShippingFormSettings(checkoutShippingFormSettingsBeanFactory.create(shippingMethodsWithCart, form));
    }
}
