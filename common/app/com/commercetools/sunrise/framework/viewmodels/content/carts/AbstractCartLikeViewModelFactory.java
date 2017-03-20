package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import io.sphere.sdk.carts.CartLike;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

public abstract class AbstractCartLikeViewModelFactory<M extends CartViewModel, I extends CartLike<?>> extends AbstractMiniCartViewModelFactory<M, I> {

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
    protected void initialize(final M viewModel, @Nullable final I cartLike) {
        super.initialize(viewModel, cartLike);
        fillSalesTax(viewModel, cartLike);
        fillSubtotalPrice(viewModel, cartLike);
        fillCustomerEmail(viewModel, cartLike);
        fillShippingAddress(viewModel, cartLike);
        fillBillingAddress(viewModel, cartLike);
        fillShippingMethod(viewModel, cartLike);
        fillPaymentDetails(viewModel, cartLike);
    }

    protected void fillSalesTax(final M viewModel, @Nullable final I cartLike) {
        final MonetaryAmount salesTax;
        if (cartLike != null) {
            salesTax = cartLike.calculateTotalAppliedTaxes()
                    .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
        } else {
            salesTax = zeroAmount(getCurrency());
        }
        viewModel.setSalesTax(getPriceFormatter().format(salesTax));
    }

    protected void fillSubtotalPrice(final M viewModel, @Nullable final I cartLike) {
        final MonetaryAmount subtotal;
        if (cartLike != null) {
            subtotal = cartLike.calculateSubTotalPrice();
        } else {
            subtotal = zeroAmount(getCurrency());
        }
        viewModel.setSubtotalPrice(getPriceFormatter().format(subtotal));
    }

    protected void fillCustomerEmail(final M viewModel, @Nullable final I cartLike) {
        if (cartLike != null) {
            viewModel.setCustomerEmail(cartLike.getCustomerEmail());
        }
    }

    protected void fillPaymentDetails(final M viewModel, @Nullable final I cartLike) {
        if (cartLike != null) {
            viewModel.setPaymentDetails(paymentInfoViewModelFactory.create(cartLike));
        }
    }

    protected void fillShippingMethod(final M viewModel, @Nullable final I cartLike) {
        if (cartLike != null) {
            viewModel.setShippingMethod(shippingInfoViewModelFactory.create(cartLike));
        }
    }

    protected void fillShippingAddress(final M viewModel, @Nullable final I cartLike) {
        if (cartLike != null) {
            viewModel.setShippingAddress(addressViewModelFactory.create(cartLike.getShippingAddress()));
        }
    }

    protected void fillBillingAddress(final M viewModel, @Nullable final I cartLike) {
        if (cartLike != null) {
            if (cartLike.getBillingAddress() != null) {
                viewModel.setBillingAddress(addressViewModelFactory.create(cartLike.getBillingAddress()));
            } else {
                viewModel.setBillingAddress(addressViewModelFactory.create(cartLike.getShippingAddress()));
            }
        }
    }
}
