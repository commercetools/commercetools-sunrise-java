package common.models;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.Product;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommonDataBuilder {
    private UserContext userContext;
    private Locale fallbackLocale = Locale.ENGLISH;
    private List<Category> rootCategories;
    private List<Product> products = Collections.emptyList();

    private CommonDataBuilder(final UserContext userContext, final List<Category> rootCategories) {
        this.userContext = userContext;
        this.rootCategories = rootCategories;
    }

    public CommonData build() {
        return new CommonData(userContext, fallbackLocale, rootCategories, products);
    }

    public CommonDataBuilder products(final List<Product> products) {
        this.products = products;
        return this;
    }

    public static CommonDataBuilder of(final UserContext userContext, final List<Category> rootCategories) {
        return new CommonDataBuilder(userContext, rootCategories);
    }
}
