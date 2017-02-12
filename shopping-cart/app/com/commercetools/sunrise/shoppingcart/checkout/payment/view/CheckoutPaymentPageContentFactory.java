package com.commercetools.sunrise.shoppingcart.checkout.payment.view;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentFormData;
import com.commercetools.sunrise.shoppingcart.checkout.payment.PaymentMethodsWithCart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutPaymentPageContentFactory extends FormPageContentFactory<CheckoutPaymentPageContent, PaymentMethodsWithCart, CheckoutPaymentFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;
    private final CheckoutPaymentFormSettingsBeanFactory checkoutPaymentFormSettingsBeanFactory;

    @Inject
    public CheckoutPaymentPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory,
                                             final CheckoutPaymentFormSettingsBeanFactory checkoutPaymentFormSettingsBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
        this.checkoutPaymentFormSettingsBeanFactory = checkoutPaymentFormSettingsBeanFactory;
    }

    @Override
    protected CheckoutPaymentPageContent getViewModelInstance() {
        return new CheckoutPaymentPageContent();
    }

    @Override
    public final CheckoutPaymentPageContent create(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return super.create(paymentMethodsWithCart, form);
    }

    protected final void initialize(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        super.initialize(model, paymentMethodsWithCart, form);
        fillCart(model, paymentMethodsWithCart, form);
        fillForm(model, paymentMethodsWithCart, form);
        fillFormSettings(model, paymentMethodsWithCart, form);
    }

    @Override
    protected void fillTitle(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        model.setTitle(pageTitleResolver.getOrEmpty("checkout:paymentPage.title"));
    }

    protected void fillCart(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        model.setCart(cartBeanFactory.create(paymentMethodsWithCart.getCart()));
    }

    protected void fillForm(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        model.setPaymentForm(form);
    }

    protected void fillFormSettings(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        model.setPaymentFormSettings(checkoutPaymentFormSettingsBeanFactory.create(paymentMethodsWithCart, form));
    }
}
