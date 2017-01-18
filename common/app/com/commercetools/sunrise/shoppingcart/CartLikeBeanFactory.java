package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.CartPriceUtils;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.products.PriceUtils;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateTotalPrice;

public abstract class CartLikeBeanFactory extends ViewModelFactory {

    private final PriceFormatter priceFormatter;

    protected CartLikeBeanFactory(final PriceFormatter priceFormatter) {
        this.priceFormatter = priceFormatter;
    }

    protected abstract LineItemBean createLineItem(final LineItem lineItem);

    protected void fillMiniCartInfo(final MiniCartBean bean, final CartLike<?> cartLike) {
        fillTotalPrice(bean, cartLike);
        fillTotalItems(bean, cartLike);
        fillLineItems(bean, cartLike);
    }

    protected void fillCartInfo(final CartBean bean, final CartLike<?> cartLike,
                                final LocalizedStringResolver localizedStringResolver, final AddressBeanFactory addressBeanFactory) {
        fillMiniCartInfo(bean, cartLike);
        fillSalesTax(bean, cartLike);
        fillSubtotalPrice(bean, cartLike);
        fillCustomerEmail(bean, cartLike);
        fillShippingAddress(bean, cartLike, addressBeanFactory);
        fillBillingAddress(bean, cartLike, addressBeanFactory);
        fillShippingMethod(bean, cartLike);
        fillPaymentDetails(bean, cartLike, localizedStringResolver);
    }

    protected void fillEmptyCartInfo(final CartBean bean, final CurrencyUnit currency) {
        bean.setTotalItems(0L);
        final MonetaryAmount zeroAmount = zeroAmount(currency);
        bean.setSalesTax(priceFormatter.format(zeroAmount));
        bean.setTotalPrice(priceFormatter.format(zeroAmount));
        bean.setSubtotalPrice(priceFormatter.format(zeroAmount));
        final ShippingInfoBean shippingMethod = new ShippingInfoBean();
        shippingMethod.setPrice(priceFormatter.format(zeroAmount));
        bean.setShippingMethod(shippingMethod);
    }

    protected void fillTotalItems(final MiniCartBean bean, final CartLike<?> cartLike) {
        bean.setTotalItems(cartLike.getLineItems().stream().mapToLong(LineItem::getQuantity).sum());
    }

    protected void fillLineItems(final MiniCartBean bean, final CartLike<?> cartLike) {
        bean.setLineItems(createLineItemList(cartLike));
    }

    protected void fillTotalPrice(final MiniCartBean bean, final CartLike<?> cartLike) {
        bean.setTotalPrice(priceFormatter.format(calculateTotalPrice(cartLike)));
    }

    protected void fillSalesTax(final CartBean bean, final CartLike<?> cartLike) {
        final MonetaryAmount amount = cartLike.calculateTotalAppliedTaxes()
                .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
        bean.setSalesTax(priceFormatter.format(amount));
    }

    protected void fillSubtotalPrice(final CartBean bean, final CartLike<?> cartLike) {
        bean.setSubtotalPrice(priceFormatter.format(cartLike.calculateSubTotalPrice()));
    }

    protected void fillCustomerEmail(final CartBean bean, final CartLike<?> cartLike) {
        bean.setCustomerEmail(cartLike.getCustomerEmail());
    }

    protected void fillPaymentDetails(final CartBean bean, final CartLike<?> cartLike, final LocalizedStringResolver localizedStringResolver) {
        bean.setPaymentDetails(createPaymentInfo(cartLike, localizedStringResolver));
    }

    protected void fillShippingMethod(final CartBean bean, final CartLike<?> cartLike) {
        bean.setShippingMethod(createShippingInfo(cartLike));
    }

    protected void fillShippingAddress(final CartBean bean, final CartLike<?> cartLike, final AddressBeanFactory addressBeanFactory) {
        bean.setShippingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
    }

    protected void fillBillingAddress(final CartBean bean, final CartLike<?> cartLike, final AddressBeanFactory addressBeanFactory) {
        if (cartLike.getBillingAddress() != null) {
            bean.setBillingAddress(addressBeanFactory.create(cartLike.getBillingAddress()));
        } else {
            bean.setBillingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
        }
    }

    protected void fillPaymentInfoType(final PaymentInfoBean bean, final CartLike<?> cartLike, final LocalizedStringResolver localizedStringResolver) {
        if (cartLike.getPaymentInfo() != null) {
            final String paymentType = cartLike.getPaymentInfo().getPayments().stream()
                    .map(Reference::getObj)
                    .filter(Objects::nonNull)
                    .map(payment -> {
                        final PaymentMethodInfo methodInfo = payment.getPaymentMethodInfo();
                        return Optional.ofNullable(methodInfo.getName())
                                .flatMap(localizedStringResolver::find)
                                .orElse(methodInfo.getMethod());
                    })
                    .findFirst()
                    .orElse(null);
            bean.setType(paymentType);
        }
    }

    protected void fillShippingInfoLabel(final ShippingInfoBean bean, final CartLike<?> cartLike) {
        if (cartLike.getShippingInfo() != null) {
            bean.setLabel(cartLike.getShippingInfo().getShippingMethodName());
        }
    }

    protected void fillShippingInfoDescription(final ShippingInfoBean bean, final CartLike<?> cartLike) {
        if (cartLike.getShippingInfo() != null) {
            final Reference<ShippingMethod> ref = cartLike.getShippingInfo().getShippingMethod();
            if (ref != null && ref.getObj() != null) {
                bean.setDescription(ref.getObj().getDescription());
            }
        }
    }

    protected void fillShippingInfoPrice(final ShippingInfoBean bean, final CartLike<?> cartLike) {
        final MonetaryAmount amount = CartPriceUtils.calculateAppliedShippingPrice(cartLike)
                .orElseGet(() -> zeroAmount(cartLike.getCurrency()));
        bean.setPrice(priceFormatter.format(amount));
    }

    private ShippingInfoBean createShippingInfo(final CartLike<?> cartLike) {
        final ShippingInfoBean bean = new ShippingInfoBean();
        fillShippingInfoLabel(bean, cartLike);
        fillShippingInfoDescription(bean, cartLike);
        fillShippingInfoPrice(bean, cartLike);
        return bean;
    }

    private PaymentInfoBean createPaymentInfo(final CartLike<?> cartLike, final LocalizedStringResolver localizedStringResolver) {
        final PaymentInfoBean bean = new PaymentInfoBean();
        fillPaymentInfoType(bean, cartLike, localizedStringResolver);
        return bean;
    }

    private LineItemListBean createLineItemList(final CartLike<?> cartLike) {
        final LineItemListBean lineItemListBean = new LineItemListBean();
        lineItemListBean.setList(cartLike.getLineItems().stream()
                .map(this::createLineItem)
                .collect(Collectors.toList()));
        return lineItemListBean;
    }

    private MonetaryAmount zeroAmount(final CurrencyUnit currency) {
        return PriceUtils.zeroAmount(currency);
    }
}
