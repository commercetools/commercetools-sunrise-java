package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HandlebarsHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
public final class CategoryNavigationComponent implements ControllerComponent, HandlebarsHook {

    private static final String CACHE_KEY = "nav-category-tree";

    private final CacheApi cacheApi;
    private final CachedCategoryTree cachedCategoryTree;
    private final CategoryTreeFilter categoryTreeFilter;
    private final CategorySettings settings;

    @Inject
    CategoryNavigationComponent(final CacheApi cacheApi, final CachedCategoryTree cachedCategoryTree,
                                final CategoryTreeFilter categoryTreeFilter, final CategorySettings settings) {
        this.cacheApi = cacheApi;
        this.cachedCategoryTree = cachedCategoryTree;
        this.categoryTreeFilter = categoryTreeFilter;
        this.settings = settings;
    }

    @Override
    public CompletionStage<Handlebars> onHandlebarsCreated(final Handlebars handlebars) {
        return getNavigationCategoryTree()
                .thenApply(categoryTree -> handlebars
                        .registerHelper("withCategoryRoots", withCategoryRootsHelper(categoryTree))
                        .registerHelper("withCategoryChildren", withCategoryChildrenHelper(categoryTree))
                        .registerHelper("ifSaleCategory", ifSaleCategoryHelper()));
    }

    private Helper<PageData> withCategoryRootsHelper(final CategoryTree categoryTree) {
        return (context, options) -> {
            final List<Category> roots = categoryTree.getRoots();
            return roots.isEmpty() ? null : options.fn(roots);
        };
    }

    private Helper<Category> withCategoryChildrenHelper(final CategoryTree categoryTree) {
        return (category, options) -> {
            final List<Category> children = categoryTree.findChildren(category);
            return children.isEmpty() ? null : options.fn(children);
        };
    }

    private Helper<Category> ifSaleCategoryHelper() {
        return (category, options) -> {
            final boolean isSale = settings.specialCategories().stream()
                    .filter(SpecialCategorySettings::isSale)
                    .anyMatch(specialCategory -> specialCategory.externalId().equals(category.getExternalId()));
            return isSale ? options.fn() : null;
        };
    }

    private CompletionStage<CategoryTree> getNavigationCategoryTree() {
        final CategoryTree nullableTree = cacheApi.get(CACHE_KEY);
        return Optional.ofNullable(nullableTree)
                .map(categoryTree -> (CompletionStage<CategoryTree>) completedFuture(categoryTree))
                .orElseGet(this::fetchAndStoreResource);
    }

    private CompletionStage<CategoryTree> fetchAndStoreResource() {
        final CompletionStage<CategoryTree> categoryTreeStage = cachedCategoryTree.get()
                .thenComposeAsync(categoryTreeFilter::filter, HttpExecution.defaultContext());
        categoryTreeStage.thenAcceptAsync(categoryTree -> cacheApi.set(CACHE_KEY, categoryTree));
        return categoryTreeStage;
    }
}
