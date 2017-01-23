package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.Configuration;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class CheckoutPaymentPageContentFactory extends PageContentFactory {

    private final String paymentFormFieldName;
    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;
    private final PaymentMethodFormFieldBeanFactory paymentMethodFormFieldBeanFactory;

    @Inject
    public CheckoutPaymentPageContentFactory(final Configuration configuration, final PageTitleResolver pageTitleResolver,
                                             final CartBeanFactory cartBeanFactory, final PaymentMethodFormFieldBeanFactory paymentMethodFormFieldBeanFactory) {
        this.paymentFormFieldName = configuration.getString("checkout.payment.formFieldName", "payment");
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
        this.paymentMethodFormFieldBeanFactory = paymentMethodFormFieldBeanFactory;
    }

    public CheckoutPaymentPageContent create(final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        final CheckoutPaymentPageContent bean = new CheckoutPaymentPageContent();
        initialize(bean, form, cart, paymentMethods);
        return bean;
    }

    protected final void initialize(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        fillTitle(bean, form, cart, paymentMethods);
        fillCart(bean, form, cart, paymentMethods);
        fillForm(bean, form, cart, paymentMethods);
        fillFormSettings(bean, form, cart, paymentMethods);
    }

    protected void fillTitle(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        bean.setTitle(pageTitleResolver.getOrEmpty("checkout:paymentPage.title"));
    }

    protected void fillCart(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        bean.setCart(cartBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        bean.setPaymentForm(form);
    }

    protected void fillFormSettings(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        final CheckoutPaymentFormSettingsBean formSettings = new CheckoutPaymentFormSettingsBean();
        formSettings.setPaymentMethod(paymentMethodFormFieldBeanFactory.create(form, paymentFormFieldName, cart, paymentMethods));
        bean.setPaymentFormSettings(formSettings);
    }
}
