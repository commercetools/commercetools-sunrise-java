package shoppingcart.checkout.payment;

import common.contexts.UserContext;
import common.models.SelectableBean;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.CartShippingInfo;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;
import java.util.Optional;

public class PaymentMethodBean extends SelectableBean {

    public PaymentMethodBean() {
    }

    public PaymentMethodBean(final PaymentMethodInfo paymentMethod, final List<String> selectedPaymentMethod,
                             final UserContext userContext) {
        super(createLabel(paymentMethod, userContext), paymentMethod.getMethod(), selectedPaymentMethod.contains(paymentMethod.getMethod()));
    }

    public PaymentMethodBean(final CartLike<?> cartLike, final MoneyContext moneyContext) {
        final CartShippingInfo shippingInfo = cartLike.getShippingInfo();
        if (shippingInfo != null) {
            setLabel(shippingInfo.getShippingMethodName());
            setSelected(true);
            final Reference<ShippingMethod> shippingMethodReference = shippingInfo.getShippingMethod();
            if (shippingMethodReference != null && shippingMethodReference.getObj() != null) {
                setDescription(shippingMethodReference.getObj().getDescription());
            }
        }
    }

    private static String createLabel(final PaymentMethodInfo paymentMethod, final UserContext userContext) {
        return Optional.ofNullable(paymentMethod.getName())
                .flatMap(name -> name.find(userContext.locales()))
                .orElse("");
    }
}
