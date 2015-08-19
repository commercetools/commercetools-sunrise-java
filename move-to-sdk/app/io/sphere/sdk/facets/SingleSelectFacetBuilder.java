package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;

import javax.annotation.Nullable;
import java.util.List;

public final class SingleSelectFacetBuilder<T> extends BaseSelectFacetBuilder<SingleSelectFacetBuilder<T>, T> implements Builder<SingleSelectFacet<T>> {

    private SingleSelectFacetBuilder(final String key, final String label, final List<FacetOption<T>> options) {
        super(key, label, options);
    }

    @Override
    public SingleSelectFacet<T> build() {
        return new SingleSelectFacet<>(getKey(), getLabel(), getOptions(), getThreshold().orElse(null), getLimit().orElse(null));
    }

    @Override
    public SingleSelectFacetBuilder<T> setThreshold(@Nullable final Long threshold) {
        return super.setThreshold(threshold);
    }

    @Override
    public SingleSelectFacetBuilder<T> setLimit(@Nullable final Long limit) {
        return super.setLimit(limit);
    }

    public static <T> SingleSelectFacetBuilder<T> of(final String key, final String label, final List<FacetOption<T>> options) {
        return new SingleSelectFacetBuilder<>(key, label, options);
    }

    @Override
    protected SingleSelectFacetBuilder<T> getThis() {
        return this;
    }
}
