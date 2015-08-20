package productcatalog.pages;

public class FilterListData {
    private final SelectFacetData sizeFacetData;
    private final SelectFacetData colorFacetData;

    public FilterListData(final SelectFacetData sizeFacetData, final SelectFacetData colorFacetData) {
        this.sizeFacetData = sizeFacetData;
        this.colorFacetData = colorFacetData;
    }

    public SelectFacetData getSize() {
        return sizeFacetData;
    }

    public SelectFacetData getColor() {
        return colorFacetData;
    }
}
