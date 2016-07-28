package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public class LineItemBeanFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private ProductVariantBeanFactory productVariantBeanFactory;

    public LineItemBean create(final LineItem lineItem) {
        final LineItemBean bean = new LineItemBean();
        initialize(bean, lineItem);
        return bean;
    }

    protected final void initialize(final LineItemBean bean, final LineItem lineItem) {
        fillTotalPrice(bean, lineItem);
        fillLineItemId(bean, lineItem);
        fillQuantity(bean, lineItem);
        fillVariant(bean, lineItem);
    }

    protected void fillLineItemId(final LineItemBean bean, final LineItem lineItem) {
        bean.setLineItemId(lineItem.getId());
    }

    protected void fillQuantity(final LineItemBean bean, final LineItem lineItem) {
        bean.setQuantity(lineItem.getQuantity());
    }

    protected void fillVariant(final LineItemBean bean, final LineItem lineItem) {
        bean.setVariant(productVariantBeanFactory.create(lineItem));
    }

    protected void fillTotalPrice(final LineItemBean bean, final LineItem lineItem) {
        final MoneyContext moneyContext = getMoneyContext(lineItem);
        bean.setTotalPrice(moneyContext.formatOrZero(lineItem.getTotalPrice()));
    }

    private MoneyContext getMoneyContext(final LineItem lineItem) {
        return MoneyContext.of(lineItem.getPrice().getValue().getCurrency(), userContext.locale());
    }
}
