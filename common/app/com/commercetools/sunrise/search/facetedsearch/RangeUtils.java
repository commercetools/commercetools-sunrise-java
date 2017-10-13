package com.commercetools.sunrise.search.facetedsearch;

import io.sphere.sdk.search.RangeFacetResult;
import io.sphere.sdk.search.model.FacetRange;
import io.sphere.sdk.search.model.FilterRange;
import io.sphere.sdk.search.model.RangeStats;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RangeUtils {

    private static final Pattern PATTERN = Pattern.compile("^\\(\\s*(?<lower>\\S+)\\s+to\\s+(?<upper>\\S+)\\s*\\)$");

    private RangeUtils() {
    }

    public static Map<String, RangeStats> mapRangeToStats(final RangeFacetResult facetResult) {
        final Map<String, RangeStats> rangeToStats = new HashMap<>();
        facetResult.getRanges()
                .forEach(stats -> {
                    final String lowerEndpoint = cleanEndpointDecimal(stats.getLowerEndpoint());
                    final String upperEndpoint = cleanEndpointDecimal(stats.getUpperEndpoint());
                    parseFacetRange(lowerEndpoint, upperEndpoint)
                            .ifPresent(range -> rangeToStats.put(range.toString(), stats));
                });
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
     * Ranges of the form {@code (* to *)} are not supported.
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
                })
                .filter(RangeUtils::hasNoBounds);
    }

    /**
     * Parses a range of the form {@code (x to y)} to a {@link FacetRange}.
     * Ranges of the form {@code (* to *)} are not supported.
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
                })
                .filter(RangeUtils::hasNoBounds);
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
        return String.format("(%s to %s)",
                boundEndpointOrAsterisk(lowerEndpoint),
                boundEndpointOrAsterisk(upperEndpoint));
    }

    @Nullable
    private static String boundEndpointOrNull(@Nullable final String endpoint) {
        return isEndpointBoundless(endpoint) ? null : endpoint;
    }

    private static String boundEndpointOrAsterisk(@Nullable final String endpoint) {
        return isEndpointBoundless(endpoint) ? "*" : endpoint;
    }

    private static boolean isEndpointBoundless(@Nullable final String endpoint) {
        return endpoint == null || endpoint.equals("*");
    }

    private static <T extends Comparable<T>> boolean hasNoBounds(final FacetRange<T> range) {
        return range.lowerBound() != null || range.upperBound() != null;
    }

    private static <T extends Comparable<T>> boolean hasNoBounds(final FilterRange<T> range) {
        return range.lowerBound() != null || range.upperBound() != null;
    }

    /**
     * Cleans the decimal part of the endpoint value due to the backend answering with decimals but not accepting them.
     * For example, the backend might answer with 9002.0, but it needs to be converted to 9002.
     * @param endpointValue the value of the endpoint to be cleaned
     * @return the value of the endpoint without the decimal part, in case the value was a number
     */
    @Nullable
    private static String cleanEndpointDecimal(@Nullable final String endpointValue) {
        if (endpointValue != null) {
            try {
                final int value = new BigDecimal(endpointValue).intValue();
                return String.valueOf(value);
            } catch (NumberFormatException e) {
                // Fallbacks to default case
            }
        }
        return endpointValue;
    }
}
