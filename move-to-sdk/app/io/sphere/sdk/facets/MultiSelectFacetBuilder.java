package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;

import javax.annotation.Nullable;
import java.util.List;

public final class MultiSelectFacetBuilder<T> extends BaseSelectFacetBuilder<MultiSelectFacetBuilder<T>, T> implements Builder<MultiSelectFacet<T>> {
    private final boolean matchesAll;

    private MultiSelectFacetBuilder(final String key, final String label, final List<FacetOption<T>> options,
                                    final boolean matchesAll) {
        super(key, label, options);
        this.matchesAll = matchesAll;
    }

    @Override
    public MultiSelectFacet<T> build() {
        return new MultiSelectFacet<>(getKey(), getLabel(), getOptions(), getThreshold().orElse(null), getLimit().orElse(null), matchesAll);
    }

    @Override
    public MultiSelectFacetBuilder<T> threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public MultiSelectFacetBuilder<T> limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public boolean matchesAll() {
        return matchesAll;
    }

    public static <T> MultiSelectFacetBuilder<T> of(final String key, final String label, final List<FacetOption<T>> options,
                                                          final boolean matchesAll) {
        return new MultiSelectFacetBuilder<>(key, label, options, matchesAll);
    }

    @Override
    protected MultiSelectFacetBuilder<T> getThis() {
        return this;
    }
}
