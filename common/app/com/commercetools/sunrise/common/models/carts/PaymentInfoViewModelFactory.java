package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class PaymentInfoViewModelFactory extends ViewModelFactory<PaymentInfoViewModel, CartLike<?>> {

    @Override
    protected PaymentInfoViewModel getViewModelInstance() {
        return new PaymentInfoViewModel();
    }

    @Override
    public final PaymentInfoViewModel create(@Nullable final CartLike<?> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final PaymentInfoViewModel model, final CartLike<?> data) {
        fillType(model, data);
    }

    protected void fillType(final PaymentInfoViewModel model, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null && cartLike.getPaymentInfo() != null) {
            final LocalizedString paymentType = cartLike.getPaymentInfo().getPayments().stream()
                    .map(Reference::getObj)
                    .filter(Objects::nonNull)
                    .map(payment -> payment.getPaymentMethodInfo().getName())
                    .findFirst()
                    .orElseGet(LocalizedString::empty);
            model.setType(paymentType);
        }
    }
}
