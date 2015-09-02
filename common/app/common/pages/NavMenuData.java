package common.pages;

import io.sphere.sdk.models.Base;

import java.util.List;

public class NavMenuData extends Base {
    private final List<CategoryData> categoryData;

    public NavMenuData(final List<CategoryData> categoryData) {
        this.categoryData = categoryData;
    }

    public List<CategoryData> getCategories() {
        return categoryData;
    }
}
