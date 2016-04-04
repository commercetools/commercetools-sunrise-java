package productcatalog.productoverview.search;

import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import productcatalog.productoverview.search.FacetBean;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FacetBeanList extends Base {
    private List<FacetBean> list;

    public FacetBeanList() {
    }

    public FacetBeanList(final List<Facet<ProductProjection>> facets) {
        this.list = facets.stream()
                .map(FacetBean::new)
                .collect(toList());
    }

    public List<FacetBean> getList() {
        return list;
    }

    public void setList(final List<FacetBean> list) {
        this.list = list;
    }
}
