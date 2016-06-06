package shoppingcart;

import common.contexts.UserContext;
import common.models.ProductAttributeBean;
import common.models.ProductAttributeBeanFactory;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.LineItem;
import wedecidelatercommon.ProductReverseRouter;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineItemBean extends MiniCartLineItemBean {

    private List<ProductAttributeBean> attributes;

    @Inject
    private ProductAttributeBeanFactory productAttributeBeanFactory;

    public LineItemBean() {
    }

    public LineItemBean(final LineItem lineItem, final ProductDataConfig productDataConfig,
                        final UserContext userContext, final ProductReverseRouter reverseRouter) {
        super(lineItem, userContext, reverseRouter);
        this.attributes = lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productDataConfig.getSelectableAttributes().contains(attr.getName()))
                .map(attr -> productAttributeBeanFactory.create(attr))
                .collect(toList());
    }

    public List<ProductAttributeBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<ProductAttributeBean> attributes) {
        this.attributes = attributes;
    }
}
