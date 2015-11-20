package productcatalog.pages;

import common.models.SelectableData;

import java.util.List;

public class SelectorData {
    private String key;
    private List<SelectableData> list;

    public SelectorData() {
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
