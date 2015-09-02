package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.TermFacetResult;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

abstract class BaseSelectFacetBuilder<T extends BaseSelectFacetBuilder<T>> extends Base {
    private final String key;
    private final String label;
    private final FacetType type;
    protected boolean multiSelect = true;
    protected boolean matchingAll = false;
    protected List<String> selectedValues = Collections.emptyList();
    protected Optional<TermFacetResult> facetResult = Optional.empty();
    protected Optional<Long> threshold = Optional.empty();
    protected Optional<Long> limit = Optional.empty();

    protected BaseSelectFacetBuilder(final String key, final String label, final FacetType type) {
        this.key = key;
        this.label = label;
        this.type = type;
    }

    public T multiSelect(final boolean multiSelect) {
        this.multiSelect = multiSelect;
        return getThis();
    }

    public T matchingAll(final boolean matchingAll) {
        this.matchingAll = matchingAll;
        return getThis();
    }

    public T selectedValues(final List<String> selectedValues) {
        this.selectedValues = selectedValues;
        return getThis();
    }

    public T facetResult(@Nullable final TermFacetResult facetResult) {
        this.facetResult = Optional.ofNullable(facetResult);
        return getThis();
    }

    public T threshold(@Nullable final Long threshold) {
        this.threshold = Optional.ofNullable(threshold);
        return getThis();
    }

    public T limit(@Nullable final Long limit) {
        this.limit = Optional.ofNullable(limit);
        return getThis();
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public FacetType getType() {
        return type;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public boolean isMatchingAll() {
        return matchingAll;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public Optional<TermFacetResult> getFacetResult() {
        return facetResult;
    }

    public Optional<Long> getThreshold() {
        return threshold;
    }

    public Optional<Long> getLimit() {
        return limit;
    }

    protected abstract T getThis();
}
