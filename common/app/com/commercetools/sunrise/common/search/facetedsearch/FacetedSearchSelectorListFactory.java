package com.commercetools.sunrise.common.search.facetedsearch;

import com.commercetools.sunrise.common.injection.RequestScoped;
import io.sphere.sdk.categories.Category;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class FacetedSearchSelectorListFactory {

    private final SelectFacetedSearchSelectorFactory selectFacetedSearchSelectorFactory;
    private final FacetedSearchConfigList facetedSearchConfigList;

    @Inject
    public FacetedSearchSelectorListFactory(final SelectFacetedSearchSelectorFactory selectFacetedSearchSelectorFactory,
                                            final FacetedSearchConfigList facetedSearchConfigList) {
        this.selectFacetedSearchSelectorFactory = selectFacetedSearchSelectorFactory;
        this.facetedSearchConfigList = facetedSearchConfigList;
    }

    public List<FacetedSearchSelector> create(final List<Category> selectedCategories) {
        final List<FacetedSearchSelector> selectors = createSelectFacetSelectors(selectedCategories);
        selectors.addAll(createRangeFacetSelectors());
        return selectors;
    }

    private List<FacetedSearchSelector> createSelectFacetSelectors(final List<Category> selectedCategories) {
        return facetedSearchConfigList.getSelectFacetedSearchConfigList().stream()
                .map(facetConfig -> selectFacetedSearchSelectorFactory.create(facetConfig, selectedCategories))
                .collect(toList());
    }

    private List<FacetedSearchSelector> createRangeFacetSelectors() {
        return Collections.emptyList(); // missing ranges
    }
}
