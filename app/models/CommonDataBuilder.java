package models;

import io.sphere.sdk.categories.Category;
import play.i18n.Lang;

import java.util.List;
import java.util.Locale;

public class CommonDataBuilder {
    private Lang lang;
    private List<Category> rootCategories;
    private Locale fallbackLocale = Locale.ENGLISH;

    private CommonDataBuilder(final Lang lang, final List<Category> rootCategories) {
        this.lang = lang;
        this.rootCategories = rootCategories;
    }

    public CommonData build() {
        return new CommonData(lang, rootCategories, fallbackLocale);
    }

    public static CommonDataBuilder of(final Lang lang, final List<Category> rootCategories) {
        return new CommonDataBuilder(lang, rootCategories);
    }
}
