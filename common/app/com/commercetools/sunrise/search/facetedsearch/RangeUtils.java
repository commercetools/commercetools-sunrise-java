package com.commercetools.sunrise.search.facetedsearch;

import io.sphere.sdk.search.RangeFacetResult;
import io.sphere.sdk.search.model.FacetRange;
import io.sphere.sdk.search.model.FilterRange;
import io.sphere.sdk.search.model.RangeStats;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RangeUtils {

    private static final Pattern PATTERN = Pattern.compile("^\\(\\s*(?<lower>\\S+)\\s+to\\s+(?<upper>\\S+)\\s*\\)$");

    private RangeUtils() {
    }

    public static Map<FacetRange<String>, RangeStats> mapRangeToStats(final RangeFacetResult facetResult) {
        final Map<FacetRange<String>, RangeStats> rangeToStats = new HashMap<>();
        facetResult.getRanges()
                .forEach(stats -> parseFacetRange(stats.getLowerEndpoint(), stats.getUpperEndpoint())
                        .ifPresent(range -> rangeToStats.put(range, stats)));
        return rangeToStats;
    }

    public static Optional<FilterRange<String>> parseFilterRange(@Nullable final String lowerEndpoint, @Nullable final String upperEndpoint) {
        final String rangeAsString = buildRange(lowerEndpoint, upperEndpoint);
        return parseFilterRange(rangeAsString);
    }

    public static Optional<FacetRange<String>> parseFacetRange(@Nullable final String lowerEndpoint, @Nullable final String upperEndpoint) {
        final String rangeAsString = buildRange(lowerEndpoint, upperEndpoint);
        return parseFacetRange(rangeAsString);
    }

    /**
     * Parses a range of the form {@code (x to y)} to a {@link FilterRange}.
     * @param rangeAsString range of the form {@code (x to y)}
     * @return the {@link FilterRange} corresponding to that range, or empty if it could not be parsed
     */
    public static Optional<FilterRange<String>> parseFilterRange(final String rangeAsString) {
        return Optional.ofNullable(parseRangeToPair(rangeAsString))
                .filter(pair -> pair.getRight() != null || pair.getLeft() != null)
                .map(pair -> {
                    if (pair.getLeft() != null && pair.getRight() != null) {
                        return FilterRange.of(pair.getLeft(), pair.getRight());
                    } else if (pair.getLeft() != null) {
                        return FilterRange.atLeast(pair.getLeft());
                    } else {
                        return FilterRange.atMost(pair.getRight());
                    }
                });
    }

    /**
     * Parses a range of the form {@code (x to y)} to a {@link FacetRange}.
     * @param rangeAsString range of the form {@code (x to y)}
     * @return the {@link FacetRange} corresponding to that range, or empty if it could not be parsed
     */
    public static Optional<FacetRange<String>> parseFacetRange(final String rangeAsString) {
        return Optional.ofNullable(parseRangeToPair(rangeAsString))
                .filter(pair -> pair.getRight() != null || pair.getLeft() != null)
                .map(pair -> {
                    if (pair.getLeft() != null && pair.getRight() != null) {
                        return FacetRange.of(pair.getLeft(), pair.getRight());
                    } else if (pair.getLeft() != null) {
                        return FacetRange.atLeast(pair.getLeft());
                    } else {
                        return FacetRange.lessThan(pair.getRight());
                    }
                });
    }

    @Nullable
    private static Pair<String, String> parseRangeToPair(final String rangeAsString) {
        final Matcher matcher = PATTERN.matcher(rangeAsString.trim());
        if (matcher.matches()) {
            final String lowerEndpoint = matcher.group("lower");
            final String upperEndpoint = matcher.group("upper");
            return ImmutablePair.of(boundEndpointOrNull(lowerEndpoint), boundEndpointOrNull(upperEndpoint));
        }
        return null;
    }

    private static String buildRange(@Nullable final String lowerEndpoint, @Nullable final String upperEndpoint) {
        return String.format("(\"%s\" to \"%s\")",
                boundEndpointOrAsterisk(lowerEndpoint),
                boundEndpointOrAsterisk(upperEndpoint));
    }

    @Nullable
    private static String boundEndpointOrNull(@Nullable final String endpoint) {
        return endpoint == null || endpoint.equals("*") ? null : endpoint;
    }

    private static String boundEndpointOrAsterisk(@Nullable final String endpoint) {
        return endpoint == null ? "*" : endpoint;
    }
}
