package io.sphere.sdk.facets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mapper that sorts the options according to the given list of values.
 * Any value that do not appear in the list of values is placed at the end of the output list.
 */
public class SortedFacetOptionMapper implements FacetOptionMapper {
    private final List<String> sortedFacetValues;

    private SortedFacetOptionMapper(final List<String> sortedFacetValues) {
        this.sortedFacetValues = sortedFacetValues;
    }

    @Override
    public List<FacetOption> apply(final List<FacetOption> facetOptions) {
        final List<FacetOption> sortedFacetOptions = new ArrayList<>(facetOptions);
        Collections.sort(sortedFacetOptions, this::comparePositions);
        return sortedFacetOptions;
    }

    public static SortedFacetOptionMapper of(final List<String> sortedFacetValues) {
        return new SortedFacetOptionMapper(sortedFacetValues);
    }

    private int comparePositions(final FacetOption left, final FacetOption right) {
        final int leftPosition = sortedFacetValues.indexOf(left.getValue());
        final int rightPosition = sortedFacetValues.indexOf(right.getValue());
        return comparePositions(leftPosition, rightPosition);
    }

    static int comparePositions(final int leftPosition, final int rightPosition) {
        final int comparison;
        if (leftPosition == rightPosition) {
            comparison = 0;
        } else if (leftPosition < 0) {
            comparison = 1;
        } else if (rightPosition < 0) {
            comparison = -1;
        } else {
            comparison = Integer.compare(leftPosition, rightPosition);
        }
        return comparison;
    }
}
