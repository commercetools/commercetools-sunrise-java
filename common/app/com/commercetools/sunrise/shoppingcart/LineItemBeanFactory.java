package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public class LineItemBeanFactory extends Base {
    @Inject
    private ProductDataConfig productDataConfig;
    @Inject
    private UserContext userContext;
    @Inject
    private ProductReverseRouter reverseRouter;

    public LineItemBean create(final LineItem lineItem) {
        return new LineItemBean(lineItem, productDataConfig, userContext, reverseRouter);
    }
}
