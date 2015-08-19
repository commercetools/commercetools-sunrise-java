package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

abstract class BaseSelectFacetUiDataBuilder<T extends BaseSelectFacetUiDataBuilder<T, V>, V> extends Base {
    private final String key;
    private final String label;
    private final List<FacetOption<V>> options;
    private Optional<Long> threshold = Optional.empty();
    private Optional<Long> limit = Optional.empty();

    protected BaseSelectFacetUiDataBuilder(final String key, final String label, final List<FacetOption<V>> options) {
        this.key = key;
        this.label = label;
        this.options = options;
    }

    public T setThreshold(@Nullable final Long threshold) {
        this.threshold = Optional.ofNullable(threshold);
        return getThis();
    }

    public T setLimit(@Nullable final Long limit) {
        this.limit = Optional.ofNullable(limit);
        return getThis();
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public List<FacetOption<V>> getOptions() {
        return options;
    }

    public Optional<Long> getThreshold() {
        return threshold;
    }

    public Optional<Long> getLimit() {
        return limit;
    }

    protected abstract T getThis();
}
