package io.sphere.sdk.facets;

import java.util.List;
import java.util.Optional;

public interface HierarchyMapper<T> {

    List<FacetOption> build(final List<FacetOption> facetOptions);

    Optional<FacetOption> buildFacetOption(final List<FacetOption> facetOptions, final T element);
}
