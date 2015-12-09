package purchase;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import common.models.ProductVariantBean;
import io.sphere.sdk.carts.CartLike;

import java.util.List;
import java.util.stream.Collectors;

public class LineItemsBean {
    private final List<ProductVariantBean> list;

    public LineItemsBean(final List<ProductVariantBean> list) {
        this.list = list;
    }

    public LineItemsBean(final CartLike<?> cartLike, final ProductDataConfig productDataConfig,
                         final UserContext userContext, final ReverseRouter reverseRouter) {
        this(cartLike.getLineItems().stream()
                .map((lineItem) -> new ProductVariantBean(lineItem, productDataConfig, userContext, reverseRouter))
                .collect(Collectors.toList()));
    }

    public List<ProductVariantBean> getList() {
        return list;
    }
}
