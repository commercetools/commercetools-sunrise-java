package com.commercetools.sunrise.ctp.categories;

import com.google.inject.ImplementedBy;
import play.Configuration;

import java.util.List;
import java.util.Optional;

@ImplementedBy(CategoriesSettingsImpl.class)
public interface CategoriesSettings {

    /**
     * @return key from the cache where to store the category tree
     */
    String cacheKey();

    /**
     * @return time in seconds until the category tree in cache expires
     */
    Optional<Integer> cacheExpiration();

    /**
     * @return whether the categories with no products assigned should be discarded from the navigation category tree
     */
    Boolean discardEmpty();

    /**
     * @return list (for multisort) of sort expressions applied when querying the categories
     */
    List<String> sortExpressions();

    /**
     * @return external ID of the category in your CTP project which children represent the navigation category tree displayed to the user
     */
    Optional<String> navigationExternalId();

    /**
     * @return external ID of the category in your CTP project to which any product tagged as new is associated
     */
    Optional<String> newExtId();

    /**
     * @return list of navigation categories that do not display products associated with these categories, but use other criteria
     */
    List<SpecialCategorySettings> specialCategories();

    static CategoriesSettings of(final Configuration globalConfig, final String configPath) {
        return new CategoriesSettingsImpl(globalConfig, configPath);
    }
}