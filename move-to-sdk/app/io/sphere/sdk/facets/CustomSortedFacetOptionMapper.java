package io.sphere.sdk.facets;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Mapper that sorts the options according to the given list of values.
 * Any value that do not appear in the list of values is placed at the end of the output list.
 */
public class CustomSortedFacetOptionMapper implements FacetOptionMapper {
    private final List<String> sortedFacetValues;

    private CustomSortedFacetOptionMapper(final List<String> sortedFacetValues) {
        this.sortedFacetValues = sortedFacetValues;
    }

    @Override
    public List<FacetOption> apply(final List<FacetOption> facetOptions) {
        return facetOptions.stream()
                .sorted(this::comparePositions)
                .collect(toList());
    }

    public static CustomSortedFacetOptionMapper of(final List<String> sortedFacetValues) {
        return new CustomSortedFacetOptionMapper(sortedFacetValues);
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
