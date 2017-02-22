package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

public abstract class AbstractCartLikeViewModelFactory<T extends CartViewModel, D extends CartLike<?>> extends AbstractMiniCartViewModelFactory<T, D> {

    private final CurrencyUnit currency;
    private final PriceFormatter priceFormatter;
    private final ShippingInfoViewModelFactory shippingInfoViewModelFactory;
    private final PaymentInfoViewModelFactory paymentInfoViewModelFactory;
    private final AddressViewModelFactory addressViewModelFactory;

    protected AbstractCartLikeViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final ShippingInfoViewModelFactory shippingInfoViewModelFactory,
                                               final PaymentInfoViewModelFactory paymentInfoViewModelFactory, final AddressViewModelFactory addressViewModelFactory) {
        super(currency, priceFormatter);
        this.currency = currency;
        this.priceFormatter = priceFormatter;
        this.shippingInfoViewModelFactory = shippingInfoViewModelFactory;
        this.paymentInfoViewModelFactory = paymentInfoViewModelFactory;
        this.addressViewModelFactory = addressViewModelFactory;
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

    protected void fillSalesTax(final T model, @Nullable final D cartLike) {
        final MonetaryAmount salesTax;
        if (cartLike != null) {
            salesTax = cartLike.calculateTotalAppliedTaxes()
                    .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
        } else {
            salesTax = zeroAmount(currency);
        }
        model.setSalesTax(priceFormatter.format(salesTax));
    }

    protected void fillSubtotalPrice(final T model, @Nullable final D cartLike) {
        final MonetaryAmount subtotal;
        if (cartLike != null) {
            subtotal = cartLike.calculateSubTotalPrice();
        } else {
            subtotal = zeroAmount(currency);
        }
        model.setSubtotalPrice(priceFormatter.format(subtotal));
    }

    protected void fillCustomerEmail(final T model, @Nullable final D cartLike) {
        if (cartLike != null) {
            model.setCustomerEmail(cartLike.getCustomerEmail());
        }
    }

    protected void fillPaymentDetails(final T model, @Nullable final D cartLike) {
        if (cartLike != null) {
            model.setPaymentDetails(paymentInfoViewModelFactory.create(cartLike));
        }
    }

    protected void fillShippingMethod(final T model, @Nullable final D cartLike) {
        if (cartLike != null) {
            model.setShippingMethod(shippingInfoViewModelFactory.create(cartLike));
        }
    }

    protected void fillShippingAddress(final T model, @Nullable final D cartLike) {
        if (cartLike != null) {
            model.setShippingAddress(addressViewModelFactory.create(cartLike.getShippingAddress()));
        }
    }

    protected void fillBillingAddress(final T model, @Nullable final D cartLike) {
        if (cartLike != null) {
            if (cartLike.getBillingAddress() != null) {
                model.setBillingAddress(addressViewModelFactory.create(cartLike.getBillingAddress()));
            } else {
                model.setBillingAddress(addressViewModelFactory.create(cartLike.getShippingAddress()));
            }
        }
    }
}
