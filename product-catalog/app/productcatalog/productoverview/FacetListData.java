package productcatalog.productoverview;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FacetListData extends Base {
    private List<FacetData> list;

    public FacetListData() {
    }

    public FacetListData(final PagedSearchResult<ProductProjection> searchResult, final List<Facet<ProductProjection>> facets) {
        this.list = facets.stream()
                .map(facet -> facet.withSearchResult(searchResult))
                .map(FacetData::new)
                .collect(toList());
    }

    public List<FacetData> getList() {
        return list;
    }

    public void setList(final List<FacetData> list) {
        this.list = list;
    }
}
