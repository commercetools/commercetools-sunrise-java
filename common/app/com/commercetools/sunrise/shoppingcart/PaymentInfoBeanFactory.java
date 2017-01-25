package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

@RequestScoped
public class PaymentInfoBeanFactory extends ViewModelFactory<PaymentInfoBean, CartLike<?>> {

    private final LocalizedStringResolver localizedStringResolver;

    @Inject
    public PaymentInfoBeanFactory(final LocalizedStringResolver localizedStringResolver) {
        this.localizedStringResolver = localizedStringResolver;
    }

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
            final String paymentType = cartLike.getPaymentInfo().getPayments().stream()
                    .map(Reference::getObj)
                    .filter(Objects::nonNull)
                    .map(this::toPaymentName)
                    .findFirst()
                    .orElse(null);
            bean.setType(paymentType);
        }
    }

    private String toPaymentName(final Payment payment) {
        final PaymentMethodInfo methodInfo = payment.getPaymentMethodInfo();
        return Optional.ofNullable(methodInfo.getName())
                .flatMap(localizedStringResolver::find)
                .orElse(methodInfo.getMethod());
    }
}
