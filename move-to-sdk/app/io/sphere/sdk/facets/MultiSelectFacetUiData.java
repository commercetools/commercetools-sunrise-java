package io.sphere.sdk.facets;

import javax.annotation.Nullable;
import java.util.List;

public final class MultiSelectFacetUiData<T> extends BaseSelectFacetUiData<T> {
    private final boolean matchesAll;

    MultiSelectFacetUiData(final String key, final String label, final List<FacetOption<T>> options,
                           @Nullable final Long threshold, @Nullable final Long limit, final boolean matchesAll) {
        super(key, label, options, threshold, limit);
        this.matchesAll = matchesAll;
    }

    @Override
    public List<FacetOption<T>> getLimitedOptions() {
        return super.getLimitedOptions();
    }

    @Override
    public boolean canBeDisplayed() {
        return super.canBeDisplayed();
    }

    /**
     * Defines whether the results should match all selected values in the facet (AND operator effect)
     * or just at least one selected value (OR operator effect)
     * @return true if results should match all selected values, false otherwise
     */
    public boolean matchesAll() {
        return matchesAll;
    }
}
