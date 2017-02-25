package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;

import java.util.List;

class FacetOptionImpl extends Base implements FacetOption {
    private final String label;
    private final String value;
    private final long count;
    private final boolean selected;
    private final List<FacetOption> children;

    FacetOptionImpl(final String label, final String value, final long count, final boolean selected, final List<FacetOption> children) {
        this.label = label;
        this.value = value;
        this.count = count;
        this.selected = selected;
        this.children = children;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public List<FacetOption> getChildren() {
        return children;
    }

    @Override
    public FacetOption withLabel(final String label) {
        return new FacetOptionImpl(label, value, count, selected, children);
    }

    @Override
    public FacetOption withValue(final String value) {
        return new FacetOptionImpl(label, value, count, selected, children);
    }

    @Override
    public FacetOption withCount(final long count) {
        return new FacetOptionImpl(label, value, count, selected, children);
    }

    @Override
    public FacetOption withSelected(final boolean selected) {
        return new FacetOptionImpl(label, value, count, selected, children);
    }

    @Override
    public FacetOption withChildren(final List<FacetOption> children) {
        return new FacetOptionImpl(label, value, count, selected, children);
    }
}
