package common.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Base;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class MiniCartLineItems extends Base {
    private List<LineItemBean> list;

    public MiniCartLineItems() {
        this.list = emptyList();
    }

    public MiniCartLineItems(final Cart cart, final UserContext userContext, final ReverseRouter reverseRouter) {
        this.list = cart.getLineItems().stream()
                .map(lineItem -> new LineItemBean(lineItem, userContext, reverseRouter))
                .collect(toList());
    }

    public List<LineItemBean> getList() {
        return list;
    }

    public void setList(final List<LineItemBean> list) {
        this.list = list;
    }
}
