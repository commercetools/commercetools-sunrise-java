package productcatalog.productoverview.search;

import common.models.SelectableData;
import io.sphere.sdk.models.Base;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DisplaySelectorBean extends Base {
    private String key;
    private List<SelectableData> list;

    public DisplaySelectorBean() {
    }

    public DisplaySelectorBean(final String key, final List<Integer> pageSizeOptions, final int currentPageSize) {
        this.key = key;
        this.list = pageSizeOptions.stream()
                .map(pageSize -> createDisplayOption(pageSize, currentPageSize))
                .collect(toList());
    }

    public DisplaySelectorBean(final String key, final List<SelectableData> list) {
        this.key = key;
        this.list = list;
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

    private static SelectableData createDisplayOption(final int pageSize, final int currentPageSize) {
        final String pageSizeAsString = String.valueOf(pageSize);
        final SelectableData selectableData = new SelectableData(pageSizeAsString, pageSizeAsString);
        if (currentPageSize == pageSize) {
            selectableData.setSelected(true);
        }
        return selectableData;
    }
}
