package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import javax.inject.Inject;
import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.common.utils.PriceUtils.*;

public abstract class CartLikeBeanFactory extends Base {

    @Inject
    protected UserContext userContext;
    @Inject
    private AddressBeanFactory addressBeanFactory;

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
        final MoneyContext moneyContext = getMoneyContext();
        bean.setSalesTax(moneyContext.formatOrZero(null));
        bean.setTotalPrice(moneyContext.formatOrZero(null));
        bean.setSubtotalPrice(moneyContext.formatOrZero(null));
        final ShippingInfoBean shippingMethod = new ShippingInfoBean();
        shippingMethod.setPrice(moneyContext.formatOrZero(null));
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
        final MoneyContext moneyContext = getMoneyContext(cartLike);
        bean.setTotalPrice(moneyContext.formatOrZero(calculateTotalPrice(cartLike)));
    }

    protected void fillSalesTax(final CartBean bean, final CartLike<?> cartLike) {
        final MoneyContext moneyContext = getMoneyContext(cartLike);
        bean.setSalesTax(moneyContext.formatOrZero(calculateSalesTax(cartLike).orElse(null)));
    }

    protected void fillSubtotalPrice(final CartBean bean, final CartLike<?> cartLike) {
        final MoneyContext moneyContext = getMoneyContext(cartLike);
        bean.setSubtotalPrice(moneyContext.formatOrZero(calculateSubTotal(cartLike)));
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
                    .filter(payment -> payment != null)
                    .map(payment -> {
                        final PaymentMethodInfo methodInfo = payment.getPaymentMethodInfo();
                        return Optional.ofNullable(methodInfo.getName())
                                .flatMap(name -> name.find(userContext.locales()))
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
        MonetaryAmount price = null;
        if (cartLike.getShippingInfo() != null) {
            price = cartLike.getShippingInfo().getPrice();
        }
        bean.setPrice(getMoneyContext(cartLike).formatOrZero(price));
    }

    protected MoneyContext getMoneyContext(final CartLike<?> cartLike) {
        return MoneyContext.of(cartLike.getCurrency(), userContext.locale());
    }

    protected MoneyContext getMoneyContext() {
        return MoneyContext.of(userContext.currency(), userContext.locale());
    }
}
