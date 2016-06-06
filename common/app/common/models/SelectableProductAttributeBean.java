package common.models;

import common.models.FormSelectableOptionBean;
import common.models.ProductAttributeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectableProductAttributeBean extends ProductAttributeBean {

    private boolean reload;
    private List<FormSelectableOptionBean> list = new ArrayList<>();
    private Map<String, Map<String, List<String>>> selectData = new HashMap<>();

    public SelectableProductAttributeBean() {
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(final boolean reload) {
        this.reload = reload;
    }

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }

    public Map<String, Map<String, List<String>>> getSelectData() {
        return selectData;
    }

    public void setSelectData(final Map<String, Map<String, List<String>>> selectData) {
        this.selectData = selectData;
    }
}
