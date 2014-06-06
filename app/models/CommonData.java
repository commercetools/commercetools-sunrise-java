package models;

import com.google.common.collect.Lists;
import play.i18n.Lang;

import java.util.List;
import java.util.Locale;
import io.sphere.sdk.categories.Category;

/**
 * A container for stuff needed in almost every template.
 */
public class CommonData {
    private final Lang lang;
    private final List<Category> rootCategories;
    private final Locale fallbackLocale;
    private final List<Locale> locales;

    CommonData(final Lang lang, final List<Category> rootCategories, Locale fallbackLocale) {
        this.lang = lang;
        this.rootCategories = rootCategories;
        this.fallbackLocale = fallbackLocale;
        locales = Lists.newArrayList(locale(), fallbackLocale);
    }

    public Lang lang() {
        return lang;
    }

    public Locale locale() {
        return lang.toLocale();
    }

    public List<Locale> locales() {
        return locales;
    }

    public List<Category> rootCategories() {
        return rootCategories;
    }

    public Locale fallbackLocale() {
        return fallbackLocale;
    }
}
