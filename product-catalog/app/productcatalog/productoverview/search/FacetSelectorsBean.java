package productcatalog.productoverview.search;

import common.contexts.UserContext;
import common.i18n.I18nResolver;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FacetSelectorsBean extends Base {
    private List<FacetSelectorBean> list;

    public FacetSelectorsBean() {
    }

    public FacetSelectorsBean(final List<FacetSelector> facetsCriteria, final PagedSearchResult<ProductProjection> searchResult,
                              final UserContext userContext, final I18nResolver i18nResolver) {
        this.list = facetsCriteria.stream()
                .map(facetCriteria -> new FacetSelectorBean(facetCriteria, searchResult, userContext, i18nResolver))
                .collect(toList());
    }

    public List<FacetSelectorBean> getList() {
        return list;
    }

    public void setList(final List<FacetSelectorBean> list) {
        this.list = list;
    }
}
