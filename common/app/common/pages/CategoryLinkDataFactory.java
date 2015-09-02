package common.pages;

import io.sphere.sdk.categories.Category;

import java.util.List;
import java.util.Locale;

public class CategoryLinkDataFactory {
    private final List<Locale> locales;

    private CategoryLinkDataFactory(final List<Locale> locales) {
        this.locales = locales;
    }

    public static CategoryLinkDataFactory of(final List<Locale> locales) {
        return new CategoryLinkDataFactory(locales);
    }

    public LinkData create(final Category category) {
        final String label = category.getName().find(locales).orElse("");
        return new LinkData(label, "");
    }
}
