package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.zones.Zone;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

public class CheckoutShippingPageContentFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;
    @Inject
    private CartBeanFactory cartLikeBeanFactory;
    @Inject
    private ShippingMethodFormFieldBeanFactory shippingMethodFormFieldBeanFactory;

    public CheckoutShippingPageContent create(final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent bean = new CheckoutShippingPageContent();
        initialize(bean, form, cart, shippingMethods);
        return bean;
    }

    protected final void initialize(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        fillTitle(bean, form, cart, shippingMethods);
        fillCart(bean, form, cart, shippingMethods);
        fillForm(bean, form, cart, shippingMethods);
    }

    protected void fillTitle(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("checkout:shippingPage.title");
        bean.setTitle(i18nResolver.getOrEmpty(userContext.locales(), i18nIdentifier));
    }

    protected void fillCart(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        bean.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        bean.setShippingForm(form);
        bean.setShippingFormSettings(createShippingFormSettings(form, cart, shippingMethods));
    }

    protected CheckoutShippingFormSettingsBean createShippingFormSettings(final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingFormSettingsBean bean = new CheckoutShippingFormSettingsBean();
        initializeShippingFormSettings(bean, form, cart, shippingMethods);
        return bean;
    }

    protected void initializeShippingFormSettings(final CheckoutShippingFormSettingsBean bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        final String fieldName = getShippingMethodFormFieldName();
        bean.setShippingMethod(shippingMethodFormFieldBeanFactory.create(form, fieldName, cart, shippingMethods));
    }

    protected String getShippingMethodFormFieldName() {
        return "shippingMethodId";
    }


}
