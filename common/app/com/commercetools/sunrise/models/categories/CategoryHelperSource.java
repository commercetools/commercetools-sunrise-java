package com.commercetools.sunrise.models.categories;

import com.github.jknack.handlebars.Options;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

@Singleton
public class CategoryHelperSource {

    private CategorySettings categorySettings;

    @Inject
    protected CategoryHelperSource(final CategorySettings categorySettings) {
        this.categorySettings = categorySettings;
    }

    public CharSequence ifSale(final Category category, final Options options) throws IOException {
        final boolean isSale = categorySettings.specialCategories().stream()
                .filter(SpecialCategorySettings::isSale)
                .anyMatch(specialCategory -> specialCategory.externalId().equals(category.getExternalId()));
        return isSale ? options.fn() : null;
    }

    public CharSequence withCategoryChildren(final Category category, final CategoryTree categoryTree,
                                             final Options options) throws IOException {
        final List<Category> children = categoryTree.findChildren(category);
        return options.fn(children);
    }
}
