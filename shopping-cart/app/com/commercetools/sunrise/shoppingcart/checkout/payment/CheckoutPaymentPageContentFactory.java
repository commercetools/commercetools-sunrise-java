package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

public class CheckoutPaymentPageContentFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;
    @Inject
    private CartBeanFactory cartLikeBeanFactory;
    @Inject
    private PaymentMethodFormFieldBeanFactory paymentMethodFormFieldBeanFactory;

    public CheckoutPaymentPageContent create(final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        final CheckoutPaymentPageContent bean = new CheckoutPaymentPageContent();
        initialize(bean, form, cart, paymentMethods);
        return bean;
    }

    protected final void initialize(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        fillTitle(bean, form, cart, paymentMethods);
        fillCart(bean, form, cart, paymentMethods);
        fillForm(bean, form, cart, paymentMethods);
    }

    protected void fillTitle(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("checkout:paymentPage.title");
        bean.setTitle(i18nResolver.getOrEmpty(userContext.locales(), i18nIdentifier));
    }

    protected void fillCart(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        bean.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutPaymentPageContent bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        bean.setPaymentForm(form);
        bean.setPaymentFormSettings(createPaymentFormSettings(form, cart, paymentMethods));
    }

    protected CheckoutPaymentFormSettingsBean createPaymentFormSettings(final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        final CheckoutPaymentFormSettingsBean bean = new CheckoutPaymentFormSettingsBean();
        initializePaymentFormSettings(bean, form, cart, paymentMethods);
        return bean;
    }

    protected void initializePaymentFormSettings(final CheckoutPaymentFormSettingsBean bean, final Form<?> form, final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        final String fieldName = getPaymentMethodFormFieldName();
        bean.setPaymentMethod(paymentMethodFormFieldBeanFactory.create(form, fieldName, cart, paymentMethods));
    }

    protected String getPaymentMethodFormFieldName() {
        return "payment";
    }
}
