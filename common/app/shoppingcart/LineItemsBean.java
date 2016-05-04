package shoppingcart;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;

import java.util.List;
import java.util.stream.Collectors;

public class LineItemsBean extends Base {

    private List<LineItemBean> list;

    public LineItemsBean() {
    }

    public LineItemsBean(final List<LineItem> lineItems, final ProductDataConfig productDataConfig,
                         final UserContext userContext, final ReverseRouter reverseRouter) {
        this.list = lineItems.stream()
                .map(lineItem -> new LineItemBean(lineItem, productDataConfig, userContext, reverseRouter))
                .collect(Collectors.toList());
    }

    public List<LineItemBean> getList() {
        return list;
    }

    public void setList(final List<LineItemBean> list) {
        this.list = list;
    }
}
