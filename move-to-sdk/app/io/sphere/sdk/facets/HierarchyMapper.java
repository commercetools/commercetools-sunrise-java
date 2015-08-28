package io.sphere.sdk.facets;

import java.util.List;
import java.util.Optional;

public interface HierarchyMapper<T> {

    List<FacetOption> build();

    Optional<FacetOption> buildFacetOption(T element);
}
