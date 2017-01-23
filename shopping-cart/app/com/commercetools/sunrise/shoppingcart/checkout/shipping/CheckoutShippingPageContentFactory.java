package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.shoppingcart.CartBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

@RequestScoped
public class CheckoutShippingPageContentFactory extends PageContentFactory {

    private final PageTitleResolver pageTitleResolver;
    private final CartBeanFactory cartBeanFactory;
    private final ShippingMethodFormFieldBeanFactory shippingMethodFormFieldBeanFactory;

    @Inject
    public CheckoutShippingPageContentFactory(final PageTitleResolver pageTitleResolver, final CartBeanFactory cartBeanFactory,
                                              final ShippingMethodFormFieldBeanFactory shippingMethodFormFieldBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartBeanFactory = cartBeanFactory;
        this.shippingMethodFormFieldBeanFactory = shippingMethodFormFieldBeanFactory;
    }

    public CheckoutShippingPageContent create(final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingPageContent bean = new CheckoutShippingPageContent();
        initialize(bean, form, cart, shippingMethods);
        return bean;
    }

    protected final void initialize(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        fillTitle(bean, form, cart, shippingMethods);
        fillCart(bean, form, cart, shippingMethods);
        fillForm(bean, form, cart, shippingMethods);
        fillFormSettings(bean, form, cart, shippingMethods);
    }

    protected void fillTitle(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        bean.setTitle(pageTitleResolver.getOrEmpty("checkout:shippingPage.title"));
    }

    protected void fillCart(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        bean.setCart(cartBeanFactory.create(cart));
    }

    protected void fillForm(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        bean.setShippingForm(form);
    }

    protected void fillFormSettings(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingFormSettingsBean formSettings = new CheckoutShippingFormSettingsBean();
        formSettings.setShippingMethod(shippingMethodFormFieldBeanFactory.create(form, cart, shippingMethods));
        bean.setShippingFormSettings(formSettings);
    }
}
