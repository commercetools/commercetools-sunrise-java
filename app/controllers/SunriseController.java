package controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import comparators.ByNameCategoryComparator;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaClient;
import io.sphere.sdk.play.controllers.ShopController;
import models.CommonDataBuilder;

import io.sphere.sdk.categories.Category;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
public class SunriseController extends ShopController {
    private final CategoryTree categoryTree;

    protected SunriseController(final PlayJavaClient client, final CategoryTree categoryTree) {
        super(client);
        this.categoryTree = categoryTree;
    }

    protected final CategoryTree categories() {
        return categoryTree;
    }

    protected final CommonDataBuilder data() {
        final ByNameCategoryComparator comparator = new ByNameCategoryComparator(lang().toLocale());
        final ImmutableList<Category> categories = Ordering.from(comparator).immutableSortedCopy(categories().getRoots());
        return CommonDataBuilder.of(lang(), categories);
    }
}
