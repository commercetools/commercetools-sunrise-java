package productcatalog.pages;

import common.pages.CategoryLinkDataFactory;
import common.pages.ReverseRouter;
import common.pages.SelectableLinkData;
import io.sphere.sdk.categories.Category;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public class BreadcrumbDataFactory {
    private final CategoryLinkDataFactory categoryLinkDataFactory;

    private BreadcrumbDataFactory(final ReverseRouter reverseRouter, final Locale locale) {
        this.categoryLinkDataFactory = CategoryLinkDataFactory.of(reverseRouter, locale);
    }

    public static BreadcrumbDataFactory of(final ReverseRouter reverseRouter, final Locale locale) {
        return new BreadcrumbDataFactory(reverseRouter, locale);
    }

    public List<SelectableLinkData> create(final List<Category> categories) {
        final List<SelectableLinkData> breadCrumbData = categories.stream()
                .map(categoryLinkDataFactory::create)
                .collect(toList());
        markLastItemSelected(breadCrumbData);
        return breadCrumbData;
    }

    private void markLastItemSelected(final List<SelectableLinkData> breadCrumbData) {
        if (!breadCrumbData.isEmpty()) {
            breadCrumbData.get(breadCrumbData.size() - 1).setSelected(true);
        }
    }
}
