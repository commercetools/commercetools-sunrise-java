package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FacetBeanList extends Base {
    private List<FacetBean> list;

    public FacetBeanList() {
    }

    public FacetBeanList(final List<FacetCriteria> facetsCriteria, final PagedSearchResult<ProductProjection> searchResult,
                         final UserContext userContext, final I18nResolver i18nResolver) {
        this.list = facetsCriteria.stream()
                .map(facetCriteria -> new FacetBean(facetCriteria, searchResult, userContext, i18nResolver))
                .collect(toList());
    }

    public List<FacetBean> getList() {
        return list;
    }

    public void setList(final List<FacetBean> list) {
        this.list = list;
    }
}
