package io.sphere.sdk.facets;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

abstract class BaseSelectFacet extends BaseFacet implements SelectFacet {
    private final List<FacetOption> options;
    private final Optional<Long> threshold;
    private final Optional<Long> limit;

    protected BaseSelectFacet(final String key, final String label, final List<FacetOption> options,
                              @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label);
        if (threshold != null && limit != null && threshold > limit) {
            throw new InvalidSelectFacetConstraintsException(threshold, limit);
        }
        this.options = options;
        this.threshold = Optional.ofNullable(threshold);
        this.limit = Optional.ofNullable(limit);
    }

    @Override
    public boolean canBeDisplayed() {
        return threshold.map(threshold -> getAllOptions().size() >= threshold).orElse(true);
    }

    /**
     * Gets the complete options without limitations.
     * @return the complete possible options
     */
    @Override
    public List<FacetOption> getAllOptions() {
        return options;
    }

    /**
     * Obtains the truncated options list according to the defined limit.
     * @return the truncated options if the limit is defined, the whole list otherwise
     */
    @Override
    public List<FacetOption> getLimitedOptions() {
        return limit.map(limit -> getAllOptions().stream().limit(limit).collect(toList()))
                .orElse(getAllOptions());
    }

    /**
     * Gets the threshold indicating the minimum amount of options allowed to be displayed in the facet.
     * @return the threshold for the amount of options that can be displayed, or absent if it has no threshold
     */
    @Override
    public Optional<Long> getThreshold() {
        return threshold;
    }

    /**
     * Gets the limit for the maximum amount of options allowed to be displayed in the facet.
     * @return the limit for the amount of options that can be displayed, or absent if it has no limit
     */
    @Override
    public Optional<Long> getLimit() {
        return limit;
    }
}
