package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;
import java.util.List;
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
        bean.setPaymentDetails(new PaymentInfoBean(cartLike.getPaymentInfo(), userContext));
    }

    protected void fillShippingMethod(final CartBean bean, final CartLike<?> cartLike) {
        final MoneyContext moneyContext = getMoneyContext(cartLike);
        bean.setShippingMethod(new ShippingInfoBean(cartLike.getShippingInfo(), moneyContext));
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

    protected MoneyContext getMoneyContext(final CartLike<?> cartLike) {
        return MoneyContext.of(cartLike.getCurrency(), userContext.locale());
    }
}
