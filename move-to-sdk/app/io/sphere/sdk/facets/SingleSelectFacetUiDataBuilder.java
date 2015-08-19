package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;

import javax.annotation.Nullable;
import java.util.List;

public final class SingleSelectFacetUiDataBuilder<T> extends BaseSelectFacetUiDataBuilder<SingleSelectFacetUiDataBuilder<T>, T> implements Builder<SingleSelectFacetUiData<T>> {

    private SingleSelectFacetUiDataBuilder(final String key, final String label, final List<FacetOption<T>> options) {
        super(key, label, options);
    }

    @Override
    public SingleSelectFacetUiData<T> build() {
        return new SingleSelectFacetUiData<>(getKey(), getLabel(), getOptions(), getThreshold().orElse(null), getLimit().orElse(null));
    }

    @Override
    public SingleSelectFacetUiDataBuilder<T> setThreshold(@Nullable final Long threshold) {
        return super.setThreshold(threshold);
    }

    @Override
    public SingleSelectFacetUiDataBuilder<T> setLimit(@Nullable final Long limit) {
        return super.setLimit(limit);
    }

    public static <T> SingleSelectFacetUiDataBuilder<T> of(final String key, final String label, final List<FacetOption<T>> options) {
        return new SingleSelectFacetUiDataBuilder<>(key, label, options);
    }

    @Override
    protected SingleSelectFacetUiDataBuilder<T> getThis() {
        return this;
    }
}
