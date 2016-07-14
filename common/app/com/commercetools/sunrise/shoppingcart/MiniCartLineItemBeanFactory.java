package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public class MiniCartLineItemBeanFactory extends Base {
    @Inject
    private UserContext userContext;
    @Inject
    private ProductVariantBeanFactory productVariantBeanFactory;

    public MiniCartLineItemBean create(final LineItem lineItem) {
        return fillBean(new MiniCartLineItemBean(), lineItem);
    }

    protected <T extends MiniCartLineItemBean> T fillBean(final T bean, final LineItem lineItem) {
        final MoneyContext moneyContext = MoneyContext.of(lineItem.getPrice().getValue().getCurrency(), userContext.locale());
        bean.setLineItemId(lineItem.getId());
        bean.setQuantity(lineItem.getQuantity());
        bean.setTotalPrice(moneyContext.formatOrZero(lineItem.getTotalPrice()));
        bean.setVariant(productVariantBeanFactory.create(lineItem));
        return bean;
    }
}
