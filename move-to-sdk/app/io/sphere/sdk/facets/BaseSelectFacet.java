package io.sphere.sdk.facets;

import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.UntypedSearchModel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

abstract class BaseSelectFacet<T> extends BaseFacet<T> implements SelectFacet<T> {
    private final boolean multiSelect;
    private final boolean matchingAll;
    private final List<String> selectedValues;
    private final Optional<TermFacetResult> termFacetResult;
    private final Optional<Long> threshold;
    private final Optional<Long> limit;

    protected BaseSelectFacet(final String key, final String label, final FacetType type, final UntypedSearchModel<T> searchModel,
                              final boolean multiSelect, final boolean matchingAll, final List<String> selectedValues,
                              @Nullable final TermFacetResult termFacetResult, @Nullable final Long threshold, @Nullable final Long limit) {
        super(key, label, type, searchModel);
        if (threshold != null && limit != null && threshold > limit) {
            throw new InvalidSelectFacetConstraintsException(threshold, limit);
        }
        this.multiSelect = multiSelect;
        this.matchingAll = matchingAll;
        this.selectedValues = selectedValues;
        this.termFacetResult = Optional.ofNullable(termFacetResult);
        this.threshold = Optional.ofNullable(threshold);
        this.limit = Optional.ofNullable(limit);
    }

    @Override
    public boolean isAvailable() {
        return threshold.map(threshold -> getAllOptions().size() >= threshold).orElse(true);
    }

    @Override
    public List<FacetOption> getAllOptions() {
        return termFacetResult.map(result -> FacetOption.ofFacetResult(result, selectedValues)).orElse(emptyList());
    }

    @Override
    public List<FacetOption> getLimitedOptions() {
        return limit.map(limit -> getAllOptions().stream().limit(limit).collect(toList()))
                .orElse(getAllOptions());
    }

    @Override
    public boolean isMultiSelect() {
        return multiSelect;
    }

    @Override
    public boolean isMatchingAll() {
        return matchingAll;
    }

    @Override
    public List<String> getSelectedValues() {
        return selectedValues;
    }

    @Override
    public Optional<TermFacetResult> getTermFacetResult() {
        return termFacetResult;
    }

    @Override
    public Optional<Long> getThreshold() {
        return threshold;
    }

    @Override
    public Optional<Long> getLimit() {
        return limit;
    }


}
