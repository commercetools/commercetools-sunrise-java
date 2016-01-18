package shoppingcart.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.stream.Collectors;

public class LineItemsBean extends Base {
    private List<ExtendedLineItemBean> list;

    public LineItemsBean() {
    }

    public LineItemsBean(final CartLike<?> cartLike, final ProductDataConfig productDataConfig,
                         final UserContext userContext, final ReverseRouter reverseRouter) {
        this.list = cartLike.getLineItems().stream()
                .map((lineItem) -> new ExtendedLineItemBean(lineItem, productDataConfig, userContext, reverseRouter))
                .collect(Collectors.toList());
    }

    public List<ExtendedLineItemBean> getList() {
        return list;
    }

    public void setList(final List<ExtendedLineItemBean> list) {
        this.list = list;
    }
}
