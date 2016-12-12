package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.CartPriceUtils;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.*;

public abstract class CartLikeBeanFactory extends ViewModelFactory {

    private final AddressBeanFactory addressBeanFactory;
    private final PriceFormatter priceFormatter;
    private final List<Locale> locales;
    private final CurrencyUnit currency;

    protected CartLikeBeanFactory(final UserContext userContext, final PriceFormatter priceFormatter,
                                  final AddressBeanFactory addressBeanFactory) {
        this.addressBeanFactory = addressBeanFactory;
        this.priceFormatter = priceFormatter;
        this.locales = userContext.locales();
        this.currency = userContext.currency();
    }

    protected abstract LineItemBean createLineItem(final LineItem lineItem);

    protected void fillMiniCartInfo(final MiniCartBean bean, final CartLike<?> cartLike) {
        fillTotalPrice(bean, cartLike);
        fillTotalItems(bean, cartLike);
        fillLineItems(bean, cartLike);
    }

    protected void fillEmptyMiniCartInfo(final MiniCartBean bean) {
        bean.setTotalItems(0L);
    }

    protected void fillCartInfo(final CartBean bean, final CartLike<?> cartLike) {
        fillMiniCartInfo(bean, cartLike);
        fillSalesTax(bean, cartLike);
        fillSubtotalPrice(bean, cartLike);
        fillCustomerEmail(bean, cartLike);
        fillShippingAddress(bean, cartLike);
        fillBillingAddress(bean, cartLike);
        fillShippingMethod(bean, cartLike);
        fillPaymentDetails(bean, cartLike);
    }

    protected void fillEmptyCartInfo(final CartBean bean) {
        fillEmptyMiniCartInfo(bean);
        final MonetaryAmount zeroAmount = zeroAmount();
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
        final LineItemListBean lineItemListBean = new LineItemListBean();
        lineItemListBean.setList(createLineItemList(cartLike));
        bean.setLineItems(lineItemListBean);
    }

    protected void fillTotalPrice(final MiniCartBean bean, final CartLike<?> cartLike) {
        bean.setTotalPrice(priceFormatter.format(calculateTotalPrice(cartLike)));
    }

    protected void fillSalesTax(final CartBean bean, final CartLike<?> cartLike) {
        final MonetaryAmount amount = calculateAppliedTaxes(cartLike).orElseGet(this::zeroAmount);
        bean.setSalesTax(priceFormatter.format(amount));
    }

    protected void fillSubtotalPrice(final CartBean bean, final CartLike<?> cartLike) {
        bean.setSubtotalPrice(priceFormatter.format(calculateSubTotalPrice(cartLike)));
    }

    protected void fillCustomerEmail(final CartBean bean, final CartLike<?> cartLike) {
        bean.setCustomerEmail(cartLike.getCustomerEmail());
    }

    protected void fillPaymentDetails(final CartBean bean, final CartLike<?> cartLike) {
        bean.setPaymentDetails(createPaymentInfo(cartLike));
    }

    protected void fillShippingMethod(final CartBean bean, final CartLike<?> cartLike) {
        bean.setShippingMethod(createShippingInfo(cartLike));
    }

    protected void fillShippingAddress(final CartBean bean, final CartLike<?> cartLike) {
        bean.setShippingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
    }

    protected void fillBillingAddress(final CartBean bean, final CartLike<?> cartLike) {
        if (cartLike.getBillingAddress() != null) {
            bean.setBillingAddress(addressBeanFactory.create(cartLike.getBillingAddress()));
        } else {
            bean.setBillingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
        }
    }

    protected List<LineItemBean> createLineItemList(final CartLike<?> cartLike) {
        return cartLike.getLineItems().stream()
                .map(this::createLineItem)
                .collect(Collectors.toList());
    }

    protected PaymentInfoBean createPaymentInfo(final CartLike<?> cartLike) {
        final PaymentInfoBean bean = new PaymentInfoBean();
        fillPaymentInfoType(bean, cartLike);
        return bean;
    }

    protected void fillPaymentInfoType(final PaymentInfoBean bean, final CartLike<?> cartLike) {
        if (cartLike.getPaymentInfo() != null) {
            final String paymentType = cartLike.getPaymentInfo().getPayments().stream()
                    .map(Reference::getObj)
                    .filter(Objects::nonNull)
                    .map(payment -> {
                        final PaymentMethodInfo methodInfo = payment.getPaymentMethodInfo();
                        return Optional.ofNullable(methodInfo.getName())
                                .flatMap(name -> name.find(locales))
                                .orElse(methodInfo.getMethod());
                    })
                    .findFirst()
                    .orElse(null);
            bean.setType(paymentType);
        }
    }

    protected ShippingInfoBean createShippingInfo(final CartLike<?> cartLike) {
        final ShippingInfoBean bean = new ShippingInfoBean();
        fillShippingInfoLabel(bean, cartLike);
        fillShippingInfoDescription(bean, cartLike);
        fillShippingInfoPrice(bean, cartLike);
        return bean;
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
        final MonetaryAmount amount = CartPriceUtils.calculateAppliedShippingPrice(cartLike).orElseGet(this::zeroAmount);
        bean.setPrice(priceFormatter.format(amount));
    }

    private MonetaryAmount zeroAmount() {
        return CartPriceUtils.zeroAmount(currency);
    }
}
