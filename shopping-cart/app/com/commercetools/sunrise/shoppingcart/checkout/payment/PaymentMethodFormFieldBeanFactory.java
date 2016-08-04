package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class PaymentMethodFormFieldBeanFactory extends Base {

    @Inject
    private UserContext userContext;

    protected PaymentMethodFormFieldBean create(final Form<?> form, final String fieldName, final Cart cart,
                                                final List<PaymentMethodInfo> paymentMethods) {
        final PaymentMethodFormFieldBean bean = new PaymentMethodFormFieldBean();
        initialize(bean, form, fieldName, cart, paymentMethods);
        return bean;
    }

    protected final void initialize(final PaymentMethodFormFieldBean bean, final Form<?> form, final String fieldName,
                                    final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        fillPaymentMethodFormFieldList(bean, form, fieldName, cart, paymentMethods);
    }

    protected void fillPaymentMethodFormFieldList(final PaymentMethodFormFieldBean bean, final Form<?> form, final String fieldName,
                                                  final Cart cart, final List<PaymentMethodInfo> paymentMethods) {
        final String selectedPaymentMethodId = getSelectedMethodId(form, fieldName);
        bean.setList(paymentMethods.stream()
                .map(shippingMethod -> createPaymentFormSelectableOption(cart, shippingMethod, selectedPaymentMethodId))
                .collect(toList()));
    }

    protected PaymentFormSelectableOptionBean createPaymentFormSelectableOption(final Cart cart, final PaymentMethodInfo paymentMethod,
                                                                                @Nullable final String selectedPaymentMethodId) {
        final PaymentFormSelectableOptionBean bean = new PaymentFormSelectableOptionBean();
        initializePaymentFormSelectableOption(bean, cart, paymentMethod, selectedPaymentMethodId);
        return bean;
    }

    protected void initializePaymentFormSelectableOption(final PaymentFormSelectableOptionBean bean, final Cart cart, final PaymentMethodInfo paymentMethod,
                                                         final @Nullable String selectedPaymentMethodId) {
        bean.setLabel(Optional.ofNullable(paymentMethod.getName())
                .flatMap(locString -> locString.find(userContext.locales()))
                .orElse("-"));
        bean.setValue(paymentMethod.getMethod());
        bean.setSelected(Objects.equals(paymentMethod.getMethod(), selectedPaymentMethodId));
    }

    @Nullable
    protected String getSelectedMethodId(final Form<?> form, final String fieldName) {
        return form.field(fieldName).value();
    }
}
