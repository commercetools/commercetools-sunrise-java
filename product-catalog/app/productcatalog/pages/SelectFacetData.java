package productcatalog.pages;

import common.pages.SelectableData;
import io.sphere.sdk.facets.SelectFacet;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SelectFacetData {
    private final SelectFacet selectFacet;

    public SelectFacetData(final SelectFacet selectFacet) {
        this.selectFacet = selectFacet;
    }

    public String getText() {
        return selectFacet.getLabel();
    }

    public List<SelectableData> getList() {
        return selectFacet.getLimitedOptions().stream()
                .map(option -> new SelectableData(option.getTerm(), null, null, null, option.isSelected()))
                .collect(toList());
    }
}
