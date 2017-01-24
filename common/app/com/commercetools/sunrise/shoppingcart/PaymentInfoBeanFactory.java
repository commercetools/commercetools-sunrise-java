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
public class PaymentInfoBeanFactory extends ViewModelFactory<PaymentInfoBean, PaymentInfoBeanFactory.Data> {

    private final LocalizedStringResolver localizedStringResolver;

    @Inject
    public PaymentInfoBeanFactory(final LocalizedStringResolver localizedStringResolver) {
        this.localizedStringResolver = localizedStringResolver;
    }

    public final PaymentInfoBean create(@Nullable final CartLike<?> cartLike) {
        final Data data = new Data(cartLike);
        return initializedViewModel(data);
    }

    @Override
    protected PaymentInfoBean getViewModelInstance() {
        return new PaymentInfoBean();
    }

    @Override
    protected final void initialize(final PaymentInfoBean bean, final Data data) {
        fillType(bean, data);
    }

    protected void fillType(final PaymentInfoBean bean, final Data data) {
        if (data.cartLike != null && data.cartLike.getPaymentInfo() != null) {
            final String paymentType = data.cartLike.getPaymentInfo().getPayments().stream()
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

    protected final static class Data {

        @Nullable
        public final CartLike<?> cartLike;

        public Data(@Nullable final CartLike<?> cartLike) {
            this.cartLike = cartLike;
        }
    }
}
