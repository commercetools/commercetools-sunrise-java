package com.commercetools.sunrise.ctp.categories;

import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
final class CategoriesSettingsImpl extends Base implements CategoriesSettings {

    private final Integer cacheExpiration;
    private final String cacheKey;
    private final Boolean discardEmpty;
    private List<String> sortExpressions;
    @Nullable
    private final String navigationExtId;
    @Nullable
    private final String newExtId;
    private final List<SpecialCategorySettings> specialCategories;

    @Inject
    CategoriesSettingsImpl(final Configuration configuration) {
        this(configuration, "sunrise.ctp.categories");
    }

    CategoriesSettingsImpl(final Configuration globalConfig, final String configPath) {
        final Configuration config = globalConfig.getConfig(configPath);
        this.cacheKey = config.getString("cacheKey");
        this.cacheExpiration = config.getInt("cacheExpiration");
        this.discardEmpty = config.getBoolean("discardEmpty");
        this.sortExpressions = config.getStringList("sortExpressions");
        this.navigationExtId = config.getString("navigationExternalId");
        this.newExtId = config.getString("newExternalId");
        this.specialCategories = config.getConfigList("specialCategories").stream()
                .map(SpecialCategorySettings::of)
                .collect(toList());
    }

    @Override
    public String cacheKey() {
        return cacheKey;
    }

    @Override
    public Optional<Integer> cacheExpiration() {
        return Optional.ofNullable(cacheExpiration);
    }

    @Override
    public Boolean discardEmpty() {
        return discardEmpty;
    }

    @Override
    public List<String> sortExpressions() {
        return sortExpressions;
    }

    @Override
    public Optional<String> navigationExternalId() {
        return Optional.ofNullable(navigationExtId);
    }

    @Override
    public Optional<String> newExtId() {
        return Optional.ofNullable(newExtId);
    }

    @Override
    public List<SpecialCategorySettings> specialCategories() {
        return specialCategories;
    }
}