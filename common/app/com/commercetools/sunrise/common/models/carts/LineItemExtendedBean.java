package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.products.ProductAttributeBean;

import java.util.List;

public class LineItemExtendedBean extends LineItemBean {

    private List<ProductAttributeBean> attributes;

    public LineItemExtendedBean() {
    }

    public List<ProductAttributeBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<ProductAttributeBean> attributes) {
        this.attributes = attributes;
    }
}
