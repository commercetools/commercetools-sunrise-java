package productcatalog.productoverview.search;

import common.models.SelectableData;
import io.sphere.sdk.models.Base;

import java.util.List;

public class SortSelectorBean extends Base {
    private String key;
    private List<SelectableData> list;

    public SortSelectorBean() {
    }

    public SortSelectorBean(final String key, final List<SelectableData> sortOptions) {
        this.key = key;
        this.list = sortOptions;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public List<SelectableData> getList() {
        return list;
    }

    public void setList(final List<SelectableData> list) {
        this.list = list;
    }
}
