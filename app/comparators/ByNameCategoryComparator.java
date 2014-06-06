package comparators;

import io.sphere.sdk.categories.Category;

import java.util.Comparator;
import java.util.Locale;

public final class ByNameCategoryComparator implements Comparator<Category> {
    private final Locale locale;

    public ByNameCategoryComparator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int compare(Category o1, Category o2) {
        return o1.getName().get(locale).or("").compareTo(o2.getName().get(locale).or(""));
    }
}
