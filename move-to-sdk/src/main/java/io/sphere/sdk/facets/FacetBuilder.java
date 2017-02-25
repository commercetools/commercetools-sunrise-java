package io.sphere.sdk.facets;

import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class FacetBuilder<T> extends Base {
    private final String key;
    protected FacetType type = DefaultFacetType.SELECT;
    @Nullable protected String label = null;
    protected boolean countHidden = false;
    protected List<String> selectedValues = Collections.emptyList();

    protected FacetBuilder(final String key) {
        this.key = key;
    }

    public T type(final FacetType type) {
        this.type = type;
        return getThis();
    }

    public T label(@Nullable final String label) {
        this.label = label;
        return getThis();
    }

    public T countHidden(final boolean countHidden) {
        this.countHidden = countHidden;
        return getThis();
    }

    public T selectedValues(final List<String> selectedValues) {
        this.selectedValues = selectedValues;
        return getThis();
    }

    public String getKey() {
        return key;
    }

    public FacetType getType() {
        return type;
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    public boolean isCountHidden() {
        return countHidden;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    abstract protected T getThis();
}
