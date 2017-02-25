package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermStats;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Interface that represents an option from a facet, as used in a faceted search.
 */
public interface FacetOption {

    /**
     * Gets the label for this facet option.
     * @return the option label
     */
    String getLabel();

    /**
     * Gets the value for this facet option.
     * @return the option value
     */
    String getValue();

    /**
     * Gets the amount of results present in this facet option.
     * @return the option count
     */
    long getCount();

    /**
     * Whether this facet option is selected or not.
     * @return true if the option is selected, false otherwise
     */
    boolean isSelected();

    /**
     * Gets the children options of this facet option.
     * @return the children options
     */
    List<FacetOption> getChildren();

    /**
     * Gets a new instance of FacetOption with the same attributes as this, but with the given label.
     * @param label the new label
     * @return a new instance with same attributes, but with the given label
     */
    FacetOption withLabel(String label);

    /**
     * Gets a new instance of FacetOption with the same attributes as this, but with the given value.
     * @param value the new value
     * @return a new instance with same attributes, but with the given value
     */
    FacetOption withValue(String value);

    /**
     * Gets a new instance of FacetOption with the same attributes as this, but with the given count.
     * @param count the new count
     * @return a new instance with same attributes, but with the given count
     */
    FacetOption withCount(long count);

    /**
     * Gets a new instance of FacetOption with the same attributes as this, but with the given selected.
     * @param selected the new selected
     * @return a new instance with same attributes, but with the given selected
     */
    FacetOption withSelected(boolean selected);

    /**
     * Gets a new instance of FacetOption with the same attributes as this, but with the given children.
     * @param children the new children
     * @return a new instance with same attributes, but with the given children
     */
    FacetOption withChildren(List<FacetOption> children);

    static FacetOption of(final String value, final long count, final boolean selected) {
        return new FacetOptionImpl(value, value, count, selected, emptyList());
    }

    static FacetOption of(final String label, final String value, final long count, final boolean selected) {
        return new FacetOptionImpl(label, value, count, selected, emptyList());
    }

    static FacetOption ofTermStats(final TermStats termStats, final List<String> selectedValues) {
        return FacetOption.of(termStats.getTerm(), termStats.getCount(), selectedValues.contains(termStats.getTerm()));
    }
}
