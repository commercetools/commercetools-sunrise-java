package io.sphere.sdk.facets;

import java.util.List;

/**
 * Mapper class that allows to transform the facet options, as they are extracted from the search result, into another list.
 * For example, to transform a list of category IDs into a hierarchical structure of categories with localized names.
 */
public interface FacetOptionMapper {

    /**
     * Transforms the given list of facet options into a different list of facet options.
     * @param facetOptions the initial options list
     * @return the transformed options list
     */
    List<FacetOption> map(final List<FacetOption> facetOptions);
}
