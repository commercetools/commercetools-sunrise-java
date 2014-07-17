package models;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.Product;
import play.i18n.Lang;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommonDataBuilder {
    private Lang lang;
    private List<Category> rootCategories;
    private Locale fallbackLocale = Locale.ENGLISH;
    private List<Product> products = Collections.emptyList();

    private CommonDataBuilder(final Lang lang, final List<Category> rootCategories) {
        this.lang = lang;
        this.rootCategories = rootCategories;
    }

    public CommonData build() {
        return new CommonData(lang, rootCategories, fallbackLocale, products);
    }

    public CommonDataBuilder products(final List<Product> products) {
        this.products = products;
        return this;
    }

    public static CommonDataBuilder of(final Lang lang, final List<Category> rootCategories) {
        return new CommonDataBuilder(lang, rootCategories);
    }
}
