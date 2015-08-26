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
    protected SelectFacetType type = SelectFacetType.SMALL;
    protected List<String> selectedValues = Collections.emptyList();
    protected Optional<TermFacetResult> termFacetResult = Optional.empty();
    protected Optional<Long> threshold = Optional.empty();
    protected Optional<Long> limit = Optional.empty();

    protected BaseSelectFacetBuilder(final String key, final String label) {
        this.key = key;
        this.label = label;
    }

    public T type(final SelectFacetType type) {
        this.type = type;
        return getThis();
    }

    public T selectedValues(final List<String> selectedValues) {
        this.selectedValues = selectedValues;
        return getThis();
    }

    public T termFacetResult(@Nullable final TermFacetResult termFacetResult) {
        this.termFacetResult = Optional.ofNullable(termFacetResult);
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

    public SelectFacetType getType() {
        return type;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public Optional<TermFacetResult> getTermFacetResult() {
        return termFacetResult;
    }

    public Optional<Long> getThreshold() {
        return threshold;
    }

    public Optional<Long> getLimit() {
        return limit;
    }

    protected abstract T getThis();
}
