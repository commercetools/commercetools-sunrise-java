package productcatalog.productoverview.search.sort;

import common.models.FormSelectableOptionBean;
import io.sphere.sdk.models.Base;

import java.util.List;

public class SortSelectorBean extends Base {

    private String key;
    private List<FormSelectableOptionBean> list;

    public SortSelectorBean() {
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
