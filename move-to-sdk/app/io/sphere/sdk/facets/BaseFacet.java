package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.model.TermFacetedSearchSearchModel;

abstract class BaseFacet<T> extends Base implements Facet<T> {
    private final String key;
    private final String label;
    private final boolean countHidden;
    private final FacetType type;
    protected final TermFacetedSearchSearchModel<T> searchModel;

    public BaseFacet(final String key, final String label, final boolean countHidden, final FacetType type, final TermFacetedSearchSearchModel<T> searchModel) {
        this.key = key;
        this.label = label;
        this.countHidden = countHidden;
        this.type = type;
        this.searchModel = searchModel;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isCountHidden() {
        return countHidden;
    }

    @Override
    public FacetType getType() {
        return type;
    }

    @Override
    public TermFacetedSearchSearchModel<T> getSearchModel() {
        return searchModel;
    }

}
