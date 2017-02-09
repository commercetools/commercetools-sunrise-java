package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class PaymentInfoBeanFactory extends ViewModelFactory<PaymentInfoBean, CartLike<?>> {

    @Override
    protected PaymentInfoBean getViewModelInstance() {
        return new PaymentInfoBean();
    }

    @Override
    public final PaymentInfoBean create(@Nullable final CartLike<?> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final PaymentInfoBean model, final CartLike<?> data) {
        fillType(model, data);
    }

    protected void fillType(final PaymentInfoBean bean, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null && cartLike.getPaymentInfo() != null) {
            final LocalizedString paymentType = cartLike.getPaymentInfo().getPayments().stream()
                    .map(Reference::getObj)
                    .filter(Objects::nonNull)
                    .map(payment -> payment.getPaymentMethodInfo().getName())
                    .findFirst()
                    .orElseGet(LocalizedString::empty);
            bean.setType(paymentType);
        }
    }
}
