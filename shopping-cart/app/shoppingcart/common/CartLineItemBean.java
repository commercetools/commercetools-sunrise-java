package shoppingcart.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductAttributeBean;
import common.models.MiniCartLineItemBean;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.LineItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CartLineItemBean extends MiniCartLineItemBean {
    private List<ProductAttributeBean> attributes;

    public CartLineItemBean() {
    }

    public CartLineItemBean(final LineItem lineItem, final ProductDataConfig productDataConfig,
                            final UserContext userContext, final ReverseRouter reverseRouter) {
        super(lineItem, userContext, reverseRouter);
        this.attributes = lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productDataConfig.getAttributeWhiteList().contains(attr.getName()))
                .map(attr -> new ProductAttributeBean(attr, productDataConfig.getMetaProductType(), userContext))
                .collect(toList());
    }

    public List<ProductAttributeBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<ProductAttributeBean> attributes) {
        this.attributes = attributes;
    }
}
