package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

public abstract class AbstractCartLikeViewModelFactory<T extends CartViewModel, D extends CartLike<?>> extends AbstractMiniCartViewModelFactory<T, D> {

    private final ShippingInfoViewModelFactory shippingInfoViewModelFactory;
    private final PaymentInfoViewModelFactory paymentInfoViewModelFactory;
    private final AddressViewModelFactory addressViewModelFactory;

    protected AbstractCartLikeViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final ShippingInfoViewModelFactory shippingInfoViewModelFactory,
                                               final PaymentInfoViewModelFactory paymentInfoViewModelFactory, final AddressViewModelFactory addressViewModelFactory) {
        super(currency, priceFormatter);
        this.shippingInfoViewModelFactory = shippingInfoViewModelFactory;
        this.paymentInfoViewModelFactory = paymentInfoViewModelFactory;
        this.addressViewModelFactory = addressViewModelFactory;
    }

    protected final ShippingInfoViewModelFactory getShippingInfoViewModelFactory() {
        return shippingInfoViewModelFactory;
    }

    protected final PaymentInfoViewModelFactory getPaymentInfoViewModelFactory() {
        return paymentInfoViewModelFactory;
    }

    protected final AddressViewModelFactory getAddressViewModelFactory() {
        return addressViewModelFactory;
    }

    @Override
    protected void initialize(final T viewModel, final D input) {
        super.initialize(viewModel, input);
        fillSalesTax(viewModel, input);
        fillSubtotalPrice(viewModel, input);
        fillCustomerEmail(viewModel, input);
        fillShippingAddress(viewModel, input);
        fillBillingAddress(viewModel, input);
        fillShippingMethod(viewModel, input);
        fillPaymentDetails(viewModel, input);
    }

    protected void fillSalesTax(final T viewModel, @Nullable final D cartLike) {
        final MonetaryAmount salesTax;
        if (cartLike != null) {
            salesTax = cartLike.calculateTotalAppliedTaxes()
                    .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
        } else {
            salesTax = zeroAmount(getCurrency());
        }
        viewModel.setSalesTax(getPriceFormatter().format(salesTax));
    }

    protected void fillSubtotalPrice(final T viewModel, @Nullable final D cartLike) {
        final MonetaryAmount subtotal;
        if (cartLike != null) {
            subtotal = cartLike.calculateSubTotalPrice();
        } else {
            subtotal = zeroAmount(getCurrency());
        }
        viewModel.setSubtotalPrice(getPriceFormatter().format(subtotal));
    }

    protected void fillCustomerEmail(final T viewModel, @Nullable final D cartLike) {
        if (cartLike != null) {
            viewModel.setCustomerEmail(cartLike.getCustomerEmail());
        }
    }

    protected void fillPaymentDetails(final T viewModel, @Nullable final D cartLike) {
        if (cartLike != null) {
            viewModel.setPaymentDetails(paymentInfoViewModelFactory.create(cartLike));
        }
    }

    protected void fillShippingMethod(final T viewModel, @Nullable final D cartLike) {
        if (cartLike != null) {
            viewModel.setShippingMethod(shippingInfoViewModelFactory.create(cartLike));
        }
    }

    protected void fillShippingAddress(final T viewModel, @Nullable final D cartLike) {
        if (cartLike != null) {
            viewModel.setShippingAddress(addressViewModelFactory.create(cartLike.getShippingAddress()));
        }
    }

    protected void fillBillingAddress(final T viewModel, @Nullable final D cartLike) {
        if (cartLike != null) {
            if (cartLike.getBillingAddress() != null) {
                viewModel.setBillingAddress(addressViewModelFactory.create(cartLike.getBillingAddress()));
            } else {
                viewModel.setBillingAddress(addressViewModelFactory.create(cartLike.getShippingAddress()));
            }
        }
    }
}
