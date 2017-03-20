package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.products.ProductAttributeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectableProductAttributeViewModel extends ProductAttributeViewModel {

    private boolean reload;
    private List<ProductAttributeFormSelectableOptionViewModel> list = new ArrayList<>();
    private Map<String, Map<String, List<String>>> selectData = new HashMap<>();

    public SelectableProductAttributeViewModel() {
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(final boolean reload) {
        this.reload = reload;
    }

    public List<ProductAttributeFormSelectableOptionViewModel> getList() {
        return list;
    }

    public void setList(final List<ProductAttributeFormSelectableOptionViewModel> list) {
        this.list = list;
    }

    public Map<String, Map<String, List<String>>> getSelectData() {
        return selectData;
    }

    public void setSelectData(final Map<String, Map<String, List<String>>> selectData) {
        this.selectData = selectData;
    }
}
