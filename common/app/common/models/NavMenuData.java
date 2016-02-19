package common.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class NavMenuData extends Base {
    private List<CategoryData> categories;

    public NavMenuData() {
    }

    public NavMenuData(final CategoryTree categoryTree, final UserContext userContext, final ReverseRouter reverseRouter,
                       @Nullable final String saleCategoryExtId) {
        this.categories = new ArrayList<>();
        categoryTree.getRoots().forEach(root -> {
            final CategoryData categoryData = createCategoryData(root, categoryTree, userContext, reverseRouter, saleCategoryExtId);
            this.categories.add(categoryData);
        });
    }

    public List<CategoryData> getCategories() {
        return categories;
    }

    public void setCategories(final List<CategoryData> categories) {
        this.categories = categories;
    }

    private static CategoryData createCategoryData(final Category category, final CategoryTree categoryTree,
                                                   final UserContext userContext, final ReverseRouter reverseRouter,
                                                   @Nullable final String saleCategoryExtId) {
        final CategoryData categoryData = new CategoryData();
        categoryData.setText(category.getName().find(userContext.locales()).orElse(""));
        categoryData.setUrl(reverseRouter.showCategoryUrlOrEmpty(userContext.locale(), category));
        categoryData.setSale(Optional.ofNullable(category.getExternalId())
                .map(id -> id.equals(saleCategoryExtId))
                .orElse(false));
        categoryData.setChildren(categoryTree.findChildren(category).stream()
                .map(child -> createCategoryData(child, categoryTree, userContext, reverseRouter, saleCategoryExtId))
                .collect(toList()));
        return categoryData;
    }
}
