package shoppingcart.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.stream.Collectors;

public class CartLineItemsBean extends Base {

    private List<CartLineItemBean> list;

    public CartLineItemsBean() {
    }

    public CartLineItemsBean(final CartLike<?> cartLike, final ProductDataConfig productDataConfig,
                             final UserContext userContext, final ReverseRouter reverseRouter) {
        this.list = cartLike.getLineItems().stream()
                .map((lineItem) -> new CartLineItemBean(lineItem, productDataConfig, userContext, reverseRouter))
                .collect(Collectors.toList());
    }

    public List<CartLineItemBean> getList() {
        return list;
    }

    public void setList(final List<CartLineItemBean> list) {
        this.list = list;
    }
}
