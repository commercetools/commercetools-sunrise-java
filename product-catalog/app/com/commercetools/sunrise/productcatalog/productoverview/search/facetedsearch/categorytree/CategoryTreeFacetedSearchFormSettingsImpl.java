package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree;

import com.commercetools.sunrise.categorytree.CategoryTreeConfiguration;
import com.commercetools.sunrise.categorytree.SpecialCategoryConfiguration;
import com.commercetools.sunrise.productcatalog.productoverview.CategoryFinder;
import com.commercetools.sunrise.search.SearchUtils;
import com.commercetools.sunrise.search.facetedsearch.AbstractFacetedSearchFormSettingsWithOptions;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.FilterExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.stream.Collectors.toList;

final class CategoryTreeFacetedSearchFormSettingsImpl extends AbstractFacetedSearchFormSettingsWithOptions<ConfiguredCategoryTreeFacetedSearchFormSettings> implements CategoryTreeFacetedSearchFormSettings {

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryTreeFacetedSearchFormSettings.class);

    private final CategoryFinder categoryFinder;
    private final CategoryTreeConfiguration categoryTreeConfiguration;
    private final CategoryTree categoryTree;

    CategoryTreeFacetedSearchFormSettingsImpl(final ConfiguredCategoryTreeFacetedSearchFormSettings settings,
                                              final Locale locale, final CategoryFinder categoryFinder,
                                              final CategoryTreeConfiguration categoryTreeConfiguration, final CategoryTree categoryTree) {
        super(settings, locale);
        this.categoryFinder = categoryFinder;
        this.categoryTreeConfiguration = categoryTreeConfiguration;
        this.categoryTree = categoryTree;
    }

    @Override
    public String getFieldName() {
        return configuration().getFieldName();
    }

    @Override
    public Optional<Category> mapFieldValueToValue(final String categoryIdentifier) {
        if (!categoryIdentifier.isEmpty()) {
            try {
                return categoryFinder.apply(categoryIdentifier)
                        .toCompletableFuture().get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                LOGGER.debug("Could not find category", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<FilterExpression<ProductProjection>> buildFilterExpressions(final Http.Context httpContext) {
        final List<SpecialCategoryConfiguration> specialCategories = categoryTreeConfiguration.specialCategories();
        if (!specialCategories.isEmpty()) {
            final Optional<Category> selectedCategory = getSelectedValue(httpContext);
            if (selectedCategory.isPresent()) {
                return specialCategories.stream()
                        .filter(config -> config.externalId().equals(selectedCategory.get().getExternalId()))
                        .findAny()
                        .map(config -> config.filterExpressions().stream()
                                .map(expression -> SearchUtils.replaceCategoryExternalId(expression, categoryTree))
                                .map(FilterExpression::<ProductProjection>of)
                                .collect(toList()))
                        .orElseGet(() -> CategoryTreeFacetedSearchFormSettings.super.buildFilterExpressions(httpContext));
            }
        }
        return CategoryTreeFacetedSearchFormSettings.super.buildFilterExpressions(httpContext);
    }
}
