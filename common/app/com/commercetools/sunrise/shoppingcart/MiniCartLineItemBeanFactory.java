package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.tobedeleted.ProductVariantBeanFactoryInjectless;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public class MiniCartLineItemBeanFactory extends Base {
    @Inject
    private UserContext userContext;
    @Inject
    private ProductReverseRouter reverseRouter;

    public MiniCartLineItemBean create(final LineItem lineItem) {
        return fillbean(new MiniCartLineItemBean(), lineItem);
    }

    protected  <T extends MiniCartLineItemBean> T fillbean(final T bean, final LineItem lineItem) {
        final MoneyContext moneyContext = MoneyContext.of(lineItem.getPrice().getValue().getCurrency(), userContext.locale());
        bean.setLineItemId(lineItem.getId());
        bean.setQuantity(lineItem.getQuantity());
        bean.setTotalPrice(moneyContext.formatOrZero(lineItem.getTotalPrice()));
        bean.setVariant(ProductVariantBeanFactoryInjectless.create(lineItem, reverseRouter, userContext));
        return bean;
    }
}
