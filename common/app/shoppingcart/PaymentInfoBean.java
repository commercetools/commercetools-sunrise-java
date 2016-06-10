package shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.carts.PaymentInfo;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;

public class PaymentInfoBean extends Base {

    private String type;

    public PaymentInfoBean() {
    }

    public PaymentInfoBean(final @Nullable PaymentInfo paymentInfo, final UserContext userContext) {
        if (paymentInfo != null) {
            this.type = paymentInfo.getPayments().stream()
                    .filter(ref -> ref.getObj() != null)
                    .map(ref -> {
                        final PaymentMethodInfo methodInfo = ref.getObj().getPaymentMethodInfo();
                        return methodInfo.getName().find(userContext.locales()).orElse(methodInfo.getMethod());
                    })
                    .findFirst() // TODO Handle multiple payments
                    .orElse(null);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
