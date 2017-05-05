package com.commercetools.sunrise.categorytree;

import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public class CategoryTreeConfiguration extends Base {

    private final Integer cacheExpiration;
    private final String cacheKey;
    private final Boolean discardEmpty;
    private List<String> sortExpressions;
    @Nullable
    private final String navigationExtId;
    @Nullable
    private final String newExtId;
    private final List<SpecialCategoryConfiguration> specialCategories;

    @Inject
    CategoryTreeConfiguration(final Configuration configuration) {
        this.cacheKey = configuration.getString("categoryTree.cacheKey");
        this.cacheExpiration = configuration.getInt("categoryTree.cacheExpiration");
        this.discardEmpty = configuration.getBoolean("categoryTree.discardEmpty");
        this.sortExpressions = configuration.getStringList("categoryTree.sortExpressions");
        this.navigationExtId = configuration.getString("categoryTree.navigationExternalId");
        this.newExtId = configuration.getString("categoryTree.newExternalId");
        this.specialCategories = configuration.getConfigList("categoryTree.specialCategories", emptyList()).stream()
                .map(SpecialCategoryConfiguration::new)
                .collect(toList());
    }

    public String cacheKey() {
        return cacheKey;
    }

    public Optional<Integer> cacheExpiration() {
        return Optional.ofNullable(cacheExpiration);
    }

    public Boolean discardEmpty() {
        return discardEmpty;
    }

    public List<String> sortExpressions() {
        return sortExpressions;
    }

    public Optional<String> navigationExternalId() {
        return Optional.ofNullable(navigationExtId);
    }

    public Optional<String> newExtId() {
        return Optional.ofNullable(newExtId);
    }

    public List<SpecialCategoryConfiguration> specialCategories() {
        return specialCategories;
    }
}
