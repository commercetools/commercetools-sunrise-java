package productcatalog.productoverview.search.facetedsearch;

import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.inject.RequestScoped;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FacetedSearchSelectorListFactory {

    @Inject
    private FacetedSearchConfigList facetedSearchConfigList;
    @Inject
    private UserContext userContext;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private RequestContext requestContext;

    public List<FacetedSearchSelector> create(final List<Category> selectedCategories) {
        final List<FacetedSearchSelector> selectors = createSelectFacetSelectors(selectedCategories);
        selectors.addAll(createRangeFacetSelectors());
        return selectors;
    }

    private List<FacetedSearchSelector> createSelectFacetSelectors(final List<Category> selectedCategories) {
        return facetedSearchConfigList.getSelectFacetedSearchConfigList().stream()
                .map(facetConfig -> SelectFacetedSearchSelectorFactory.of(facetConfig, userContext, requestContext, categoryTree, selectedCategories).create())
                .collect(toList());
    }

    private List<FacetedSearchSelector> createRangeFacetSelectors() {
        return Collections.emptyList(); // missing ranges
    }
}
