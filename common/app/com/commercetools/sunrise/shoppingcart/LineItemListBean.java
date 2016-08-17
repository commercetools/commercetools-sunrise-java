package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ModelBean;

import java.util.List;

public class LineItemListBean extends ModelBean {

    private List<LineItemBean> list;

    public LineItemListBean() {
    }

    public List<LineItemBean> getList() {
        return list;
    }

    public void setList(final List<LineItemBean> list) {
        this.list = list;
    }
}
