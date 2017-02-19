package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.addresses.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

public abstract class AbstractCartLikeBeanFactory<T extends CartBean, D extends CartLike<?>> extends AbstractMiniCartBeanFactory<T, D> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;
    private final ShippingInfoBeanFactory shippingInfoBeanFactory;
    private final PaymentInfoBeanFactory paymentInfoBeanFactory;
    private final AddressBeanFactory addressBeanFactory;

    protected AbstractCartLikeBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final ShippingInfoBeanFactory shippingInfoBeanFactory,
                                          final PaymentInfoBeanFactory paymentInfoBeanFactory, final AddressBeanFactory addressBeanFactory) {
        super(currency, priceFormatter);
        this.currency = currency;
        this.priceFormatter = priceFormatter;
        this.shippingInfoBeanFactory = shippingInfoBeanFactory;
        this.paymentInfoBeanFactory = paymentInfoBeanFactory;
        this.addressBeanFactory = addressBeanFactory;
    }

    @Override
    protected void initialize(final T model, final D data) {
        super.initialize(model, data);
        fillSalesTax(model, data);
        fillSubtotalPrice(model, data);
        fillCustomerEmail(model, data);
        fillShippingAddress(model, data);
        fillBillingAddress(model, data);
        fillShippingMethod(model, data);
        fillPaymentDetails(model, data);
    }

    protected void fillSalesTax(final T bean, @Nullable final D cartLike) {
        final MonetaryAmount salesTax;
        if (cartLike != null) {
            salesTax = cartLike.calculateTotalAppliedTaxes()
                    .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
        } else {
            salesTax = zeroAmount(currency);
        }
        bean.setSalesTax(priceFormatter.format(salesTax));
    }

    protected void fillSubtotalPrice(final T bean, @Nullable final D cartLike) {
        final MonetaryAmount subtotal;
        if (cartLike != null) {
            subtotal = cartLike.calculateSubTotalPrice();
        } else {
            subtotal = zeroAmount(currency);
        }
        bean.setSubtotalPrice(priceFormatter.format(subtotal));
    }

    protected void fillCustomerEmail(final T bean, @Nullable final D cartLike) {
        if (cartLike != null) {
            bean.setCustomerEmail(cartLike.getCustomerEmail());
        }
    }

    protected void fillPaymentDetails(final T bean, @Nullable final D cartLike) {
        if (cartLike != null) {
            bean.setPaymentDetails(paymentInfoBeanFactory.create(cartLike));
        }
    }

    protected void fillShippingMethod(final T bean, @Nullable final D cartLike) {
        if (cartLike != null) {
            bean.setShippingMethod(shippingInfoBeanFactory.create(cartLike));
        }
    }

    protected void fillShippingAddress(final T bean, @Nullable final D cartLike) {
        if (cartLike != null) {
            bean.setShippingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
        }
    }

    protected void fillBillingAddress(final T bean, @Nullable final D cartLike) {
        if (cartLike != null) {
            if (cartLike.getBillingAddress() != null) {
                bean.setBillingAddress(addressBeanFactory.create(cartLike.getBillingAddress()));
            } else {
                bean.setBillingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
            }
        }
    }
}
