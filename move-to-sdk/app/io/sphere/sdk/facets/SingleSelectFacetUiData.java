package io.sphere.sdk.facets;

import javax.annotation.Nullable;
import java.util.List;

public final class SingleSelectFacetUiData<T> extends BaseSelectFacetUiData<T> {

    SingleSelectFacetUiData(final String key, final String label, final List<FacetOption<T>> options,
                            @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label, options, threshold, limit);
    }

    @Override
    public List<FacetOption<T>> getLimitedOptions() {
        return super.getLimitedOptions();
    }

    @Override
    public boolean canBeDisplayed() {
        return super.canBeDisplayed();
    }
}
