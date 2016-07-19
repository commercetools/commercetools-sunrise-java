package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.forms.UserFeedback;
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
        final CheckoutShippingPageContent bean = new CheckoutShippingPageContent();
        initialize(bean, form, cart, shippingMethods);
        return bean;
    }

    protected final void initialize(final CheckoutShippingPageContent bean, final Form<?> form, final Cart cart, final List<ShippingMethod> shippingMethods) {
        fillForm(bean, form, shippingMethods);
        fillTitle(bean, cart);
        fillCart(bean, cart);
    }

    protected void fillCart(final CheckoutShippingPageContent bean, final Cart cart) {
        bean.setCart(cartLikeBeanFactory.create(cart));
    }

    protected void fillTitle(final CheckoutShippingPageContent bean, final Cart cart) {
        bean.setTitle(i18nResolver.getOrEmpty(userContext.locales(), I18nIdentifier.of("checkout:shippingPage.title")));
    }

    protected void fillForm(final CheckoutShippingPageContent bean, final Form<?> form, final List<ShippingMethod> shippingMethods) {
        bean.setShippingForm(createShippingForm(form, shippingMethods)); // TODO Use Play form instead
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
