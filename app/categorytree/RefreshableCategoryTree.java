package categorytree;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.CategoryTreeExtended;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Identifiable;
import io.sphere.sdk.queries.QueryAll;
import org.apache.commons.lang3.ObjectUtils;
import play.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public final class RefreshableCategoryTree extends Base implements CategoryTreeExtended {
    private CategoryTreeExtended categoryTree;
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
    public List<Category> getSiblings(final Collection<Category> categoryIds) {
        return categoryTree.getSiblings(categoryIds);
    }

    @Override
    public CategoryTree getSubtree(final Collection<Category> parentCategories) {
        return categoryTree.getSubtree(parentCategories);
    }

    @Override
    public Category getRootAncestor(final Category category) {
        return categoryTree.getRootAncestor(category);
    }

    public static RefreshableCategoryTree of(final SphereClient sphereClient) {
        return new RefreshableCategoryTree(sphereClient);
    }

    private static CategoryTreeExtended fetchFreshCategoryTree(final SphereClient client) {
        final List<Category> categories = fetchCategories(client);
        Logger.debug("Provide CategoryTree with " + categories.size() + " categories");
        return CategoryTreeExtended.of(categories);
    }

    private static List<Category> fetchCategories(final SphereClient client) {
        final List<Category> categories = QueryAll.of(CategoryQuery.of()).run(client).toCompletableFuture().join();
        return sortCategories(categories);
    }

    private static List<Category> sortCategories(final List<Category> categories) {
        return categories.stream()
                .sorted((c1, c2) -> ObjectUtils.compare(c1.getOrderHint(), c2.getOrderHint()))
                .collect(toList());
    }
}
