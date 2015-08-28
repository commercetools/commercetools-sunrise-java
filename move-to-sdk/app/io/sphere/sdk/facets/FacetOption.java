package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class FacetOption extends Base {
    private final String value;
    private final long count;
    private final boolean selected;
    private final List<FacetOption> children;

    FacetOption(final String value, final long count, final boolean selected, final List<FacetOption> children) {
        this.value = value;
        this.count = count;
        this.selected = selected;
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public long getCount() {
        return count;
    }

    public boolean isSelected() {
        return selected;
    }

    public List<FacetOption> getChildren() {
        return children;
    }

    public FacetOption withValue(final String value) {
        return new FacetOption(value, count, selected, children);
    }

    public FacetOption withCount(final long count) {
        return new FacetOption(value, count, selected, children);
    }

    public FacetOption withSelected(final boolean selected) {
        return new FacetOption(value, count, selected, children);
    }

    public FacetOption withChildren(final List<FacetOption> children) {
        return new FacetOption(value, count, selected, children);
    }

    public static FacetOption of(final String value, final long count, final boolean selected) {
        return new FacetOption(value, count, selected, emptyList());
    }

    public static FacetOption ofTermStats(final TermStats termStats, final List<String> selectedValues) {
        return FacetOption.of(termStats.getTerm(), termStats.getCount(), selectedValues.contains(termStats.getTerm()));
    }

    public static List<FacetOption> ofFacetResult(final TermFacetResult facetResult, final List<String> selectedValues) {
        return facetResult.getTerms().stream()
                .map(termStats -> ofTermStats(termStats, selectedValues))
                .collect(toList());
    }
}
