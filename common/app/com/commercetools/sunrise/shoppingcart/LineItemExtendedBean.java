package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ProductAttributeBean;

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
