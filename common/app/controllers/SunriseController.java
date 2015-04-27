package controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import countries.CountryOperations;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.client.PlayJavaSphereClient;
import models.CommonDataBuilder;

import io.sphere.sdk.categories.Category;
import models.UserContext;
import play.Configuration;
import play.mvc.Controller;
import io.sphere.sdk.play.controllers.ShopController;

/**
 * An application specific controller.
 * Since we want to show a standard web shop it contains categories.
 */
public class SunriseController extends ShopController {
    private final CategoryTree categoryTree;
    private final CountryOperations countryOperations;

    protected SunriseController(final PlayJavaSphereClient client, final CategoryTree categoryTree, final Configuration configuration) {
        super(client);
        this.categoryTree = categoryTree;
        this.countryOperations = CountryOperations.of(configuration);
    }

    protected final CategoryTree categories() {
        return categoryTree;
    }

    protected final CommonDataBuilder data() {
        final ByNameCategoryComparator comparator = new ByNameCategoryComparator(Controller.lang().toLocale());
        final ImmutableList<Category> categories = Ordering.from(comparator).immutableSortedCopy(categories().getRoots());
        return CommonDataBuilder.of(userContext(), categories);
    }

    protected final UserContext userContext() {
        return UserContext.of(Controller.lang(), countryOperations.country());
    }
}
