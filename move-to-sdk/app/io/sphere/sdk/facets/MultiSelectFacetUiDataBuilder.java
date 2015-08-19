package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;

import javax.annotation.Nullable;
import java.util.List;

public final class MultiSelectFacetUiDataBuilder<T> extends BaseSelectFacetUiDataBuilder<MultiSelectFacetUiDataBuilder<T>, T> implements Builder<MultiSelectFacetUiData<T>> {
    private final boolean matchesAll;

    private MultiSelectFacetUiDataBuilder(final String key, final String label, final List<FacetOption<T>> options,
                                          final boolean matchesAll) {
        super(key, label, options);
        this.matchesAll = matchesAll;
    }

    @Override
    public MultiSelectFacetUiData<T> build() {
        return new MultiSelectFacetUiData<>(getKey(), getLabel(), getOptions(), getThreshold().orElse(null), getLimit().orElse(null), matchesAll);
    }

    @Override
    public MultiSelectFacetUiDataBuilder<T> setThreshold(@Nullable final Long threshold) {
        return super.setThreshold(threshold);
    }

    @Override
    public MultiSelectFacetUiDataBuilder<T> setLimit(@Nullable final Long limit) {
        return super.setLimit(limit);
    }

    public boolean matchesAll() {
        return matchesAll;
    }

    public static <T> MultiSelectFacetUiDataBuilder<T> of(final String key, final String label, final List<FacetOption<T>> options,
                                                          final boolean matchesAll) {
        return new MultiSelectFacetUiDataBuilder<>(key, label, options, matchesAll);
    }

    @Override
    protected MultiSelectFacetUiDataBuilder<T> getThis() {
        return this;
    }
}
