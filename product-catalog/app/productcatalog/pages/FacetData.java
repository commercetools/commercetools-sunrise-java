package productcatalog.pages;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public abstract class FacetData<T> {

    public String getText() {
        return getSelectFacet().getLabel();
    }

    public abstract List<T> getList();

    protected abstract Facet<ProductProjection> getSelectFacet();
}
