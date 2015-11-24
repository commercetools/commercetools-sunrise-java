package controllers;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Identifiable;
import io.sphere.sdk.queries.QueryAll;
import org.apache.commons.lang3.ObjectUtils;
import play.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public final class RefreshableCategoryTree extends Base implements CategoryTree {
    private CategoryTree currentCategoryTree;
    private SphereClient sphereClient;

    private RefreshableCategoryTree(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
        refresh();
    }

    public static RefreshableCategoryTree of(final SphereClient sphereClient) {
        return new RefreshableCategoryTree(sphereClient);
    }

    @Override
    public List<Category> getRoots() {
        return currentCategoryTree.getRoots();
    }

    @Override
    public Optional<Category> findById(final String id) {
        return currentCategoryTree.findById(id);
    }

    @Override
    public Optional<Category> findByExternalId(final String externalId) {
        return currentCategoryTree.findByExternalId(externalId);
    }

    @Override
    public Optional<Category> findBySlug(final Locale locale, final String slug) {
        return currentCategoryTree.findBySlug(locale, slug);
    }

    @Override
    public List<Category> getAllAsFlatList() {
        return currentCategoryTree.getAllAsFlatList();
    }

    @Override
    public List<Category> findChildren(final Identifiable<Category> category) {
        return currentCategoryTree.findChildren(category);
    }

    public synchronized void refresh() {
        this.currentCategoryTree = fetchFreshCategoryTree(sphereClient);
    }

    private static CategoryTree fetchFreshCategoryTree(final SphereClient client) {
        final QueryAll<Category, CategoryQuery> query = QueryAll.of(CategoryQuery.of());
        final List<Category> categories = query.run(client).toCompletableFuture().join().stream()
                .sorted((c1, c2) -> ObjectUtils.compare(c1.getOrderHint(), c2.getOrderHint())).collect(toList());
        Logger.debug("Provide CategoryTree with " + categories.size() + " categories");
        return CategoryTree.of(categories);
    }
}
