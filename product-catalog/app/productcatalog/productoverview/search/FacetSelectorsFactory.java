package productcatalog.productoverview.search;

import common.contexts.UserContext;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class FacetSelectorsFactory {

    private final FacetConfigList configList;
    private final Map<String, List<String>> queryString;
    private final List<Category> selectedCategories;
    private final UserContext userContext;
    private final CategoryTree categoryTree;

    public FacetSelectorsFactory(final FacetConfigList configList, final Map<String, List<String>> queryString,
                                 final List<Category> selectedCategories, final UserContext userContext, final CategoryTree categoryTree) {
        this.configList = configList;
        this.queryString = queryString;
        this.selectedCategories = selectedCategories;
        this.userContext = userContext;
        this.categoryTree = categoryTree;
    }

    public static FacetSelectorsFactory of(final FacetConfigList configList, final Map<String, List<String>> queryString,
                                    final List<Category> selectedCategories, final UserContext userContext, final CategoryTree categoryTree) {
        return new FacetSelectorsFactory(configList, queryString, selectedCategories, userContext, categoryTree);
    }

    public List<FacetSelector> create() {
        final List<FacetSelector> selectors = createSelectFacetSelectors();
        selectors.addAll(createRangeFacetSelectors());
        return selectors;
    }

    private List<FacetSelector> createSelectFacetSelectors() {
        return configList.getSelectFacetConfigs().stream()
                .map(facetConfig -> SelectFacetSelectorFactory.of(facetConfig, queryString, selectedCategories, userContext, categoryTree).create())
                .collect(toList());
    }

    private List<FacetSelector> createRangeFacetSelectors() {
        return Collections.emptyList(); // missing ranges
    }
}
