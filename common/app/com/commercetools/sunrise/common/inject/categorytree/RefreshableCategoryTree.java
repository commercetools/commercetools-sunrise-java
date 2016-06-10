package com.commercetools.sunrise.common.inject.categorytree;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Identifiable;
import org.apache.commons.lang3.ObjectUtils;
import play.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static io.sphere.sdk.queries.QueryExecutionUtils.queryAll;
import static java.util.stream.Collectors.toList;

public final class RefreshableCategoryTree extends Base implements CategoryTree {
    private CategoryTree categoryTree;
    private SphereClient sphereClient;

    private RefreshableCategoryTree(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
        refresh();
    }

    public synchronized void refresh() {
        this.categoryTree = fetchFreshCategoryTree(sphereClient);
    }

    @Override
    public List<Category> getRoots() {
        return categoryTree.getRoots();
    }

    @Override
    public Optional<Category> findById(final String id) {
        return categoryTree.findById(id);
    }

    @Override
    public Optional<Category> findByExternalId(final String externalId) {
        return categoryTree.findByExternalId(externalId);
    }

    @Override
    public Optional<Category> findBySlug(final Locale locale, final String slug) {
        return categoryTree.findBySlug(locale, slug);
    }

    @Override
    public List<Category> getAllAsFlatList() {
        return categoryTree.getAllAsFlatList();
    }

    @Override
    public List<Category> findChildren(final Identifiable<Category> category) {
        return categoryTree.findChildren(category);
    }

    @Override
    public List<Category> findSiblings(final Collection<? extends Identifiable<Category>> collection) {
        return categoryTree.findSiblings(collection);
    }

    @Override
    public CategoryTree getSubtree(final Collection<? extends Identifiable<Category>> collection) {
        return categoryTree.getSubtree(collection);
    }

    @Override
    public Category getRootAncestor(final Identifiable<Category> identifiable) {
        return categoryTree.getRootAncestor(identifiable);
    }

    @Override
    public List<Category> getSubtreeRoots() {
        return categoryTree.getSubtreeRoots();
    }

    public static RefreshableCategoryTree of(final SphereClient sphereClient) {
        return new RefreshableCategoryTree(sphereClient);
    }

    private static CategoryTree fetchFreshCategoryTree(final SphereClient client) {
        final List<Category> categories = fetchCategories(client);
        Logger.debug("Provide CategoryTree with " + categories.size() + " categories");
        return CategoryTree.of(categories);
    }

    private static List<Category> fetchCategories(final SphereClient client) {
        final List<Category> categories = blockingWait(queryAll(client, CategoryQuery.of()), 30, TimeUnit.SECONDS);
        return sortCategories(categories);
    }

    private static List<Category> sortCategories(final List<Category> categories) {
        return categories.stream()
                .sorted((c1, c2) -> ObjectUtils.compare(c1.getOrderHint(), c2.getOrderHint()))
                .collect(toList());
    }
}
