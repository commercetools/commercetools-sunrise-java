package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

public class CartLikeBeanFactory extends Base {
    @Inject
    protected UserContext userContext;
    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    private ProductReverseRouter productReverseRouter;

    public CartLikeBean create(final CartLike<?> cartLike) {
        return new CartLikeBean(cartLike, userContext, productDataConfig, productReverseRouter);
    }
}
