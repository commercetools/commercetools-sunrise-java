package com.commercetools.sunrise.utils;

import java.util.List;

public final class SortUtils {

    private SortUtils() {
    }

    public static <T> int comparePositions(final T left, final T right, final List<T> customSortedValues) {
        final int leftPosition = customSortedValues.indexOf(left);
        final int rightPosition = customSortedValues.indexOf(right);
        return comparePositions(leftPosition, rightPosition);
    }

    static int comparePositions(final int leftPosition, final int rightPosition) {
        final int comparison;
        if (leftPosition == rightPosition) {
            comparison = 0;
        } else if (leftPosition < 0) {
            comparison = 1;
        } else if (rightPosition < 0) {
            comparison = -1;
        } else {
            comparison = Integer.compare(leftPosition, rightPosition);
        }
        return comparison;
    }
}
