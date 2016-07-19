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
        final MiniCartLineItemBean bean = new MiniCartLineItemBean();
        initialize(bean, lineItem);
        return bean;
    }

    protected final void initialize(final MiniCartLineItemBean bean, final LineItem lineItem) {
        final MoneyContext moneyContext = MoneyContext.of(lineItem.getPrice().getValue().getCurrency(), userContext.locale());
        bean.setLineItemId(lineItem.getId());
        bean.setQuantity(lineItem.getQuantity());
        bean.setTotalPrice(moneyContext.formatOrZero(lineItem.getTotalPrice()));
        bean.setVariant(productVariantBeanFactory.create(lineItem));
    }
}
