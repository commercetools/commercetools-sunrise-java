package com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage;

import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.models.ModelBean;

import java.util.List;

public class ProductsPerPageSelectorBean extends ModelBean {

    private String key;
    private List<FormSelectableOptionBean> list;

    public ProductsPerPageSelectorBean() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }
}
