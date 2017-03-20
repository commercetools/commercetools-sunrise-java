package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class PaymentInfoViewModelFactory extends SimpleViewModelFactory<PaymentInfoViewModel, CartLike<?>> {

    @Override
    protected PaymentInfoViewModel newViewModelInstance(@Nullable final CartLike<?> cartLike) {
        return new PaymentInfoViewModel();
    }

    @Override
    public final PaymentInfoViewModel create(@Nullable final CartLike<?> cartLike) {
        return super.create(cartLike);
    }

    @Override
    protected final void initialize(final PaymentInfoViewModel viewModel, @Nullable final CartLike<?> cartLike) {
        fillType(viewModel, cartLike);
    }

    protected void fillType(final PaymentInfoViewModel viewModel, @Nullable final CartLike<?> cartLike) {
        if (cartLike != null && cartLike.getPaymentInfo() != null) {
            final LocalizedString paymentType = cartLike.getPaymentInfo().getPayments().stream()
                    .map(Reference::getObj)
                    .filter(Objects::nonNull)
                    .map(payment -> payment.getPaymentMethodInfo().getName())
                    .findFirst()
                    .orElseGet(LocalizedString::empty);
            viewModel.setType(paymentType);
        }
    }
}
