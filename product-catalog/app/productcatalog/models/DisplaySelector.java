package productcatalog.models;

import common.models.SelectableData;
import io.sphere.sdk.models.Base;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DisplaySelector extends Base {
    private List<SelectableData> list;

    public DisplaySelector() {
    }

    public DisplaySelector(final List<Integer> pageSizeOptions, final int currentPageSize) {
        this.list = pageSizeOptions.stream()
                .map(pageSize -> createDisplayOption(pageSize, currentPageSize))
                .collect(toList());
    }

    public DisplaySelector(final List<SelectableData> list) {
        this.list = list;
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
