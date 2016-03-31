package common.utils;

import javax.annotation.Nullable;

public final class SunriseArrayUtils {

    private SunriseArrayUtils() {
    }

    @Nullable
    public static String getArrayElement(final String[] array, final int position, final String defaultValue) {
        final boolean containsPosition = array.length > position;
        return containsPosition ? array[position] : defaultValue;
    }
}
