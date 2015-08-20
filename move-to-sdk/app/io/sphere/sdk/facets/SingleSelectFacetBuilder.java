package io.sphere.sdk.facets;

import io.sphere.sdk.models.Builder;

import javax.annotation.Nullable;
import java.util.List;

public final class SingleSelectFacetBuilder extends BaseSelectFacetBuilder<SingleSelectFacetBuilder> implements Builder<SingleSelectFacet> {

    private SingleSelectFacetBuilder(final String key, final String label, final List<FacetOption> options) {
        super(key, label, options);
    }

    @Override
    public SingleSelectFacet build() {
        return new SingleSelectFacet(getKey(), getLabel(), getOptions(), getThreshold().orElse(null), getLimit().orElse(null));
    }

    @Override
    public SingleSelectFacetBuilder threshold(@Nullable final Long threshold) {
        return super.threshold(threshold);
    }

    @Override
    public SingleSelectFacetBuilder limit(@Nullable final Long limit) {
        return super.limit(limit);
    }

    public static <T> SingleSelectFacetBuilder of(final String key, final String label, final List<FacetOption> options) {
        return new SingleSelectFacetBuilder(key, label, options);
    }

    @Override
    protected SingleSelectFacetBuilder getThis() {
        return this;
    }
}
