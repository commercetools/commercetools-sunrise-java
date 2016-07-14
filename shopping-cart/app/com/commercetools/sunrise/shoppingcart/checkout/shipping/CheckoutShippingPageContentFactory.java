package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.UserFeedback;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import play.data.Form;

import javax.inject.Inject;
import java.util.List;

import static com.commercetools.sunrise.common.forms.FormUtils.extractFormField;
import static java.util.stream.Collectors.toList;

public class CheckoutShippingPageContentFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private UserFeedback userFeedback;
    @Inject
    protected CartLikeBeanFactory cartLikeBeanFactory;

    public CheckoutShippingPageContent create(final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        return fillBean(new CheckoutShippingPageContent(), form, cart, shippingMethods);
    }

    protected <T extends CheckoutShippingPageContent> T fillBean(final T pageContent, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        fillForm(pageContent, form, shippingMethods);
        fillTitle(pageContent, cart);
        fillCart(pageContent, cart);
        return pageContent;
    }

    protected void fillCart(final CheckoutShippingPageContent pageContent, final Cart cart) {
        pageContent.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillTitle(final PageContent pageContent, final Cart cart) {
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
    }

    protected void fillForm(final CheckoutShippingPageContent pageContent, final Form<?> form, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingFormBean bean = createShippingForm(form, shippingMethods);
        userFeedback.findErrors().ifPresent(bean::setErrors);
        pageContent.setShippingForm(bean);
    }

    protected CheckoutShippingFormBean createShippingForm(final Form<?> form, final List<ShippingMethod> shippingMethods) {
        final CheckoutShippingFormBean bean = new CheckoutShippingFormBean();
        final String shippingMethodId = extractFormField(form, "shippingMethodId");
        final ShippingMethodsFormBean formBean = createShippingMethods(shippingMethods, shippingMethodId);
        bean.setShippingMethods(formBean);
        return bean;
    }

    protected ShippingMethodsFormBean createShippingMethods(final List<ShippingMethod> shippingMethods, final String selectedShippingMethodId) {
        final ShippingMethodsFormBean bean = new ShippingMethodsFormBean();
        bean.setList(shippingMethods.stream()
                .map(shippingMethod -> new ShippingMethodBean(shippingMethod, selectedShippingMethodId))
                .collect(toList()));
        return bean;
    }
}
