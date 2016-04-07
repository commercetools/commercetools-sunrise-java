package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;

import javax.annotation.Nullable;
import java.util.Optional;

abstract class BaseFacet<T> extends Base implements Facet<T> {
    private final String key;
    private final FacetType type;
    private final Optional<String> label;
    private final boolean countHidden;
    protected final TermFacetedSearchSearchModel<T> searchModel;

    protected BaseFacet(final String key, final FacetType type, final boolean countHidden, @Nullable final String label,
                        final TermFacetedSearchSearchModel<T> searchModel) {
        this.key = key;
        this.type = type;
        this.label = Optional.ofNullable(label);
        this.countHidden = countHidden;
        this.searchModel = searchModel;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public FacetType getType() {
        return type;
    }

    @Override
    public Optional<String> getLabel() {
        return label;
    }

    @Override
    public boolean isCountHidden() {
        return countHidden;
    }

    @Override
    public TermFacetedSearchSearchModel<T> getSearchModel() {
        return searchModel;
    }

}
