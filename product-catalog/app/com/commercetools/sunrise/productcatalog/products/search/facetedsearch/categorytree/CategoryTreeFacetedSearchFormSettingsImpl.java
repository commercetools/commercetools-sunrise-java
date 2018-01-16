package com.commercetools.sunrise.productcatalog.products.search.facetedsearch.categorytree;

import com.commercetools.sunrise.models.categories.CategorySettings;
import com.commercetools.sunrise.models.categories.SpecialCategorySettings;
import com.commercetools.sunrise.models.search.SearchUtils;
import com.commercetools.sunrise.models.search.facetedsearch.AbstractFacetedSearchFormSettingsWithOptions;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.FilterExpression;
import play.mvc.Http;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

final class CategoryTreeFacetedSearchFormSettingsImpl extends AbstractFacetedSearchFormSettingsWithOptions<ConfiguredCategoryTreeFacetedSearchFormSettings> implements CategoryTreeFacetedSearchFormSettings {

    private final CategorySettings categorySettings;
    private final CategoryTree categoryTree;

    CategoryTreeFacetedSearchFormSettingsImpl(final ConfiguredCategoryTreeFacetedSearchFormSettings settings,
                                              final Locale locale, final CategorySettings categorySettings,
                                              final CategoryTree categoryTree) {
        super(settings, locale);
        this.categorySettings = categorySettings;
        this.categoryTree = categoryTree;
    }

    @Override
    public String getFieldName() {
        return configuration().getFieldName();
    }

    @Override
    public Optional<Category> mapFieldValueToValue(final String categoryIdentifier) {
        if (!categoryIdentifier.isEmpty()) {
            return categoryTree.findBySlug(getLocale(), categoryIdentifier); // TODO hide slug implementation
        }
        return Optional.empty();
    }

    @Override
    public List<FilterExpression<ProductProjection>> buildFilterExpressions(final Http.Context httpContext) {
        final List<SpecialCategorySettings> specialCategories = categorySettings.specialCategories();
        if (!specialCategories.isEmpty()) {
            final Optional<Category> selectedCategory = getSelectedValue(httpContext);
            if (selectedCategory.isPresent()) {
                return specialCategories.stream()
                        .filter(config -> config.externalId().equals(selectedCategory.get().getExternalId()))
                        .findAny()
                        .map(config -> config.productFilterExpressions().stream()
                                .map(expression -> SearchUtils.replaceCategoryExternalId(expression, categoryTree))
                                .map(FilterExpression::<ProductProjection>of)
                                .collect(toList()))
                        .orElseGet(() -> CategoryTreeFacetedSearchFormSettings.super.buildFilterExpressions(httpContext));
            }
        }
        return CategoryTreeFacetedSearchFormSettings.super.buildFilterExpressions(httpContext);
    }
}