package com.commercetools.sunrise.search.facetedsearch.bucketranges;

import io.sphere.sdk.search.model.FacetRange;
import io.sphere.sdk.search.model.FilterRange;

import java.util.List;
import java.util.Optional;

import static com.commercetools.sunrise.search.facetedsearch.RangeUtils.parseFacetRange;
import static com.commercetools.sunrise.search.facetedsearch.RangeUtils.parseFilterRange;
import static java.util.stream.Collectors.toList;

public final class BucketRangeUtils {

    public static List<FilterRange<String>> optionsToFilterRange(final List<BucketRangeFacetedSearchFormOption> rangeOptions) {
        return rangeOptions.stream()
                .map(BucketRangeUtils::optionToFilterRange)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    public static List<FacetRange<String>> optionsToFacetRange(final List<BucketRangeFacetedSearchFormOption> rangeOptions) {
        return rangeOptions.stream()
                .map(BucketRangeUtils::optionToFacetRange)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    public static Optional<FilterRange<String>> optionToFilterRange(final BucketRangeFacetedSearchFormOption rangeOption) {
        return parseFilterRange(rangeOption.getValue())
                .filter(range -> range.isBounded());
    }

    public static Optional<FacetRange<String>> optionToFacetRange(final BucketRangeFacetedSearchFormOption rangeOption) {
        return parseFacetRange(rangeOption.getValue())
                .filter(range -> range.isBounded());
    }
}
