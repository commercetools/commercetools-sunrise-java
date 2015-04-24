package common.models;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Locale;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;

/**
 * A container for stuff needed in almost every template.
 */
public class CommonData {
    private final UserContext userContext;
    private final Locale fallbackLocale;
    private final List<Locale> locales;
    private final List<Category> rootCategories;
    private final List<ProductProjection> products;

    CommonData(final UserContext userContext, final Locale fallbackLocale, final List<Category> rootCategories,
               final List<ProductProjection> products) {
        this.userContext = userContext;
        this.rootCategories = rootCategories;
        this.products = products;
        this.fallbackLocale = fallbackLocale;
        locales = Lists.newArrayList(userContext.locale(), fallbackLocale);
    }

    public UserContext userContext() {
        return userContext;
    }

    public Locale fallbackLocale() {
        return fallbackLocale;
    }

    public List<Locale> locales() {
        return locales;
    }

    public List<Category> rootCategories() {
        return rootCategories;
    }

    public List<ProductProjection> products() {
        return products;
    }
}
