package shoppingcart.checkout.payment;

import common.contexts.UserContext;
import common.models.SelectableBean;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;
import java.util.Optional;

public class PaymentMethodBean extends SelectableBean {

    public PaymentMethodBean() {
    }

    public PaymentMethodBean(final PaymentMethodInfo paymentMethod, final List<String> selectedPaymentMethod,
                             final UserContext userContext) {
        super(createLabel(paymentMethod, userContext), paymentMethod.getMethod(), selectedPaymentMethod.contains(paymentMethod.getMethod()));
    }

    private static String createLabel(final PaymentMethodInfo paymentMethod, final UserContext userContext) {
        return Optional.ofNullable(paymentMethod.getName())
                .flatMap(name -> name.find(userContext.locales()))
                .orElse("");
    }
}
