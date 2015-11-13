package purchase;

import common.contexts.UserContext;
import io.sphere.sdk.carts.CartLike;

import java.util.List;
import java.util.stream.Collectors;

public class LineItemsBean {
    private final List<ProductVariantBean> list;

    public LineItemsBean(final List<ProductVariantBean> list) {
        this.list = list;
    }

    public LineItemsBean(final CartLike<?> cartLike, final UserContext userContext) {
        this(cartLike.getLineItems().stream().map((lineItem) -> new ProductVariantBean(lineItem, userContext)).collect(Collectors.toList()));
    }

    public List<ProductVariantBean> getList() {
        return list;
    }
}
