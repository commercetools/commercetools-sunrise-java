package io.sphere.sdk.facets;

public class InvalidSelectFacetConstraintsException extends RuntimeException {

    public InvalidSelectFacetConstraintsException(final Long threshold, final Long limit) {
        super(String.format("The threshold cannot be higher than the limit. Threshold: %d, Limit: %d", threshold, limit));
    }
}
