package productcatalog.pages;

import common.pages.SelectableData;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.facets.SelectFacet;
import io.sphere.sdk.facets.SelectFacetType;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class SelectFacetData extends FacetData<SelectableData> {
    protected final SelectFacet<ProductProjection> selectFacet;

    public SelectFacetData(final SelectFacet<ProductProjection> selectFacet) {
        this.selectFacet = selectFacet;
    }

    @Override
    public List<SelectableData> getList() {
        return selectFacet.getLimitedOptions().stream()
                .map(option -> new SelectableData(option.getTerm(), null, null, null, option.isSelected()))
                .collect(toList());
    }

    @Override
    public SelectFacet<ProductProjection> getSelectFacet() {
        return selectFacet;
    }

    public boolean getTypeSelectSmall() {
        return selectFacet.getType().equals(SelectFacetType.SMALL);
    }

    public boolean getTypeSelectLarge() {
        return selectFacet.getType().equals(SelectFacetType.LARGE);
    }

}
