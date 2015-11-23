package common.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class NavMenuData extends Base {
    private List<CategoryData> categoryData;

    public NavMenuData() {
    }

    public NavMenuData(final CategoryTree categoryTree, final UserContext userContext, final ReverseRouter reverseRouter,
                       @Nullable final String saleCategoryExtId) {
        this.categoryData = new ArrayList<>();
        categoryTree.getRoots().forEach(root -> {
            final CategoryData categoryData = createCategoryData(root, categoryTree, userContext, reverseRouter, saleCategoryExtId);
            this.categoryData.add(categoryData);
        });
    }

    public List<CategoryData> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(final List<CategoryData> categoryData) {
        this.categoryData = categoryData;
    }

    private static CategoryData createCategoryData(final Category category, final CategoryTree categoryTree,
                                                   final UserContext userContext, final ReverseRouter reverseRouter,
                                                   @Nullable final String saleCategoryExtId) {
        final CategoryData categoryData = new CategoryData();
        categoryData.setText(category.getName().find(userContext.locales()).orElse(""));
        categoryData.setUrl(getCategoryUrl(category, userContext.locale(), reverseRouter));
        categoryData.setSale(Optional.ofNullable(category.getExternalId())
                .map(id -> id.equals(saleCategoryExtId))
                .orElse(false));
        categoryData.setChildren(categoryTree.findChildren(category).stream()
                .map(child -> createCategoryData(child, categoryTree, userContext, reverseRouter, saleCategoryExtId))
                .collect(toList()));
        return categoryData;
    }

    private static String getCategoryUrl(final Category category, final Locale locale, final ReverseRouter reverseRouter) {
        final String slug = category.getSlug().find(locale).orElse("");
        return reverseRouter.category(locale.toLanguageTag(), slug).url();
    }
}
