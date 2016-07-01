package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.WithOverridablePageContent;
import com.commercetools.sunrise.common.forms.UserFeedback;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutAddressPageContentFactory extends Base implements WithOverridablePageContent<CheckoutAddressPageContent> {

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private UserFeedback userFeedback;
    @Inject
    private CheckoutAddressFormBeanFactory checkoutAddressFormBeanFactory;
    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;
    @Inject
    private CheckoutAddressFormSettingsFactory addressFormSettingsFactory;


    @Override
    public CheckoutAddressPageContent createPageContent() {
        return new CheckoutAddressPageContent();
    }

    public CheckoutAddressPageContent create(final Form<?> form, final Cart cart) {
        final CheckoutAddressPageContent pageContent = createPageContent();
        fillTitle(pageContent, cart);
        fillCart(pageContent, cart);
        fillForm(pageContent, form);
        fillFormSettings(pageContent, form);
        return pageContent;
    }

    protected void fillFormSettings(final CheckoutAddressPageContent pageContent, final Form<?> form) {
        pageContent.setAddressFormSettings(addressFormSettingsFactory.create(form));
    }

    protected void fillForm(final CheckoutAddressPageContent pageContent, final Form<?> form) {
        final CheckoutAddressFormBean bean = checkoutAddressFormBeanFactory.create(form);
        userFeedback.findErrors().ifPresent(bean::setErrors);
        pageContent.setAddressForm(form);
    }

    protected void fillCart(final CheckoutAddressPageContent pageContent, final Cart cart) {
        pageContent.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillTitle(final CheckoutAddressPageContent pageContent, final Cart cart) {
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
    }
}
