package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.UntypedSearchModel;

abstract class BaseFacet<T> extends Base implements Facet<T> {
    private final String key;
    private final String label;
    protected final UntypedSearchModel<T> searchModel;

    public BaseFacet(final String key, final String label, final UntypedSearchModel<T> searchModel) {
        this.key = key;
        this.label = label;
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
    public UntypedSearchModel<T> getSearchModel() {
        return searchModel;
    }
}
