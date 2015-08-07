package io.sphere.sdk.facets;

import io.sphere.sdk.search.FacetExpression;

abstract class BaseFacetUI<T> implements FacetUI<T> {
    private final String key;
    private final String label;
    private final FacetExpression<T> expression;

    protected BaseFacetUI(final String key, final String label, final FacetExpression<T> expression) {
        this.key = key;
        this.label = label;
        this.expression = expression;
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
    public FacetExpression<T> getExpression() {
        return expression;
    }
}
