package common.pages;

import io.sphere.sdk.categories.Category;

import java.util.Locale;

public class CategoryLinkDataFactory {
    private final ReverseRouter reverseRouter;
    private final Locale locale;

    private CategoryLinkDataFactory(final ReverseRouter reverseRouter, final Locale locale) {
        this.reverseRouter = reverseRouter;
        this.locale = locale;
    }

    public static CategoryLinkDataFactory of(final ReverseRouter reverseRouter, final Locale locale) {
        return new CategoryLinkDataFactory(reverseRouter, locale);
    }

    public SelectableLinkData create(final Category category) {
        final String label = getLabel(category);
        final String url = getUrl(category);

        return new SelectableLinkData(label, url, false);
    }

    private String getLabel(final Category category) {
        return category.getName().get(locale);
    }

    private String getUrl(final Category category) {
        return category.getSlug().find(locale)
                .map(slug -> reverseRouter.category(locale.getLanguage(), slug, 1).url())
                .orElse("");
    }
}