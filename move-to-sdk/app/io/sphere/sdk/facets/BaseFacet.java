package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;

abstract class BaseFacet extends Base implements Facet {
    private final String key;
    private final String label;

    public BaseFacet(final String key, final String label) {
        this.key = key;
        this.label = label;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
