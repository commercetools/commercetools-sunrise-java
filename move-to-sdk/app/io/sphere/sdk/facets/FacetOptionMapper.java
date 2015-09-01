package io.sphere.sdk.facets;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Mapper class that allows to transform the facet options, as they are extracted from the search result, into another list.
 * For example, to transform a list of category IDs into a hierarchical structure of categories with localized names.
 */
@FunctionalInterface
public interface FacetOptionMapper extends UnaryOperator<List<FacetOption>> {

    /**
     * Transforms the given list of facet options into a different list of facet options.
     * @param facetOptions the initial options list
     * @return the transformed options list
     */
    @Override
    List<FacetOption> apply(List<FacetOption> facetOptions);
}
