package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.products.PriceUtils;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

public abstract class AbstractCartBeanFactory<T extends CartBean, C extends CartLike<?>> extends AbstractMiniCartBeanFactory<T, C> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;
    private final ShippingInfoBeanFactory shippingInfoBeanFactory;
    private final PaymentInfoBeanFactory paymentInfoBeanFactory;
    private final AddressBeanFactory addressBeanFactory;

    protected AbstractCartBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final ShippingInfoBeanFactory shippingInfoBeanFactory,
                                      final PaymentInfoBeanFactory paymentInfoBeanFactory, final AddressBeanFactory addressBeanFactory) {
        super(currency, priceFormatter);
        this.currency = currency;
        this.priceFormatter = priceFormatter;
        this.shippingInfoBeanFactory = shippingInfoBeanFactory;
        this.paymentInfoBeanFactory = paymentInfoBeanFactory;
        this.addressBeanFactory = addressBeanFactory;
    }

    @Override
    protected void initialize(final T bean, final Data<C> data) {
        super.initialize(bean, data);
        fillSalesTax(bean, data);
        fillSubtotalPrice(bean, data);
        fillCustomerEmail(bean, data);
        fillShippingAddress(bean, data);
        fillBillingAddress(bean, data);
        fillShippingMethod(bean, data);
        fillPaymentDetails(bean, data);
    }

    protected void fillSalesTax(final T bean, final Data<C> data) {
        final MonetaryAmount salesTax;
        if (data.cartLike != null) {
            salesTax = data.cartLike.calculateTotalAppliedTaxes()
                    .orElseGet(() -> zeroAmount(data.cartLike.getCurrency()));
        } else {
            salesTax = zeroAmount(currency);
        }
        bean.setSalesTax(priceFormatter.format(salesTax));
    }

    protected void fillSubtotalPrice(final T bean, final Data<C> data) {
        final MonetaryAmount subtotal;
        if (data.cartLike != null) {
            subtotal = data.cartLike.calculateSubTotalPrice();
        } else {
            subtotal = zeroAmount(currency);
        }
        bean.setSubtotalPrice(priceFormatter.format(subtotal));
    }

    protected void fillCustomerEmail(final T bean, final Data<C> data) {
        if (data.cartLike != null) {
            bean.setCustomerEmail(data.cartLike.getCustomerEmail());
        }
    }

    protected void fillPaymentDetails(final T bean, final Data<C> data) {
        if (data.cartLike != null) {
            bean.setPaymentDetails(paymentInfoBeanFactory.create(data.cartLike));
        }
    }

    protected void fillShippingMethod(final T bean, final Data<C> data) {
        if (data.cartLike != null) {
            bean.setShippingMethod(shippingInfoBeanFactory.create(data.cartLike));
        }
    }

    protected void fillShippingAddress(final T bean, final Data<C> data) {
        if (data.cartLike != null) {
            bean.setShippingAddress(addressBeanFactory.create(data.cartLike.getShippingAddress()));
        }
    }

    protected void fillBillingAddress(final T bean, final Data<C> data) {
        if (data.cartLike != null) {
            if (data.cartLike.getBillingAddress() != null) {
                bean.setBillingAddress(addressBeanFactory.create(data.cartLike.getBillingAddress()));
            } else {
                bean.setBillingAddress(addressBeanFactory.create(data.cartLike.getShippingAddress()));
            }
        }
    }

    private MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return PriceUtils.zeroAmount(currency);
    }
}
