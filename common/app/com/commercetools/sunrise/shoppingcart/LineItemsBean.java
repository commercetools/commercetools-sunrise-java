package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.models.Base;

import java.util.List;

public class LineItemsBean extends Base {

    private List<LineItemBean> list;

    public LineItemsBean() {
    }

    public List<LineItemBean> getList() {
        return list;
    }

    public void setList(final List<LineItemBean> list) {
        this.list = list;
    }
}
