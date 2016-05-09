package common.utils;

import javax.annotation.Nullable;
import java.util.List;

import static java.util.Arrays.asList;

public final class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * Given a string array and a particular position, it returns the array element in that position
     * or the default value if the array does not contain an element for that position.
     * @param array the string array from which to get the element
     * @param position the position of the element
     * @param defaultValue the default value returned in case there is no element in that position
     * @return the element from the array in that position, or the default value if there is no element
     */
    @Nullable
    public static String getArrayElement(final String[] array, final int position, final String defaultValue) {
        final boolean containsPosition = array.length > position;
        return containsPosition ? array[position] : defaultValue;
    }

    public static <T> List<T> arrayToList(final T[] array) {
        return asList(array);
    }
}
