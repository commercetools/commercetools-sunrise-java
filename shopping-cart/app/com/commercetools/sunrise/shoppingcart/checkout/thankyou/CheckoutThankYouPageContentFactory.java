package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.OrderBeanFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class CheckoutThankYouPageContentFactory extends Base {

    @Inject
    protected OrderBeanFactory orderBeanFactory;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;
    @Inject
    private UserContext userContext;

    public CheckoutThankYouPageContent create(final Order order) {
        final CheckoutThankYouPageContent bean = new CheckoutThankYouPageContent();
        initialize(bean, order);
        return bean;
    }

    protected final void initialize(final CheckoutThankYouPageContent bean, final Order order) {
        fillOrder(bean, order);
        fillTitle(bean);
    }

    protected void fillOrder(final CheckoutThankYouPageContent bean, final Order order) {
        bean.setOrder(orderBeanFactory.create(order));
    }

    protected void fillTitle(final CheckoutThankYouPageContent bean) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("checkout:thankYouPage.title");
        bean.setTitle(i18nResolver.getOrEmpty(userContext.locales(), i18nIdentifier));
    }

}
